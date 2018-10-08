import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Calculate the national shift using Daniel Zhu's method of interpolating with 2016 turnout data.
 */
public class DZhuNatlShiftCalc implements NationalShiftCalculator {

    /**
     * Map of district names to how many people voted in that district in 2014, plus interpolation for districts that
     * weren't contested in 2014.
     */
    private final Map<String, Integer> districtToVoteMap;

    /**
     * Default constructor.
     *
     * @param congressionalTurnout2014 The file with the turnout for the 2014 congressional elections.
     * @param congressionalTurnout2016 The file with the turnout for the 2016 congressional elections.
     * @param redistrict2018           The set of postal codes for the states that were redistricted between 2016 and
     *                                 2018.
     * @param redistrict2016           The set of postal codes for the states that were redistricted between 2014 and
     *                                 2016.
     * @throws IOException If either turnout file is missing/improperly formatted.
     */
    public DZhuNatlShiftCalc(String congressionalTurnout2014, String congressionalTurnout2016,
                             Set<String> redistrict2018, Set<String> redistrict2016) throws IOException {
        //Define line up here to avoid garbage collection
        String line;

        int total2016 = 0;
        BufferedReader turnout2016FileReader = new BufferedReader(new FileReader(congressionalTurnout2016));
        //Clear header
        turnout2016FileReader.readLine();
        Map<String, Integer> districtToVoteMap2016 = new HashMap<>();
        while ((line = turnout2016FileReader.readLine()) != null) {
            String[] splitLine = line.split(",");
            int votes = Integer.parseInt(splitLine[1]);
            //Sum and record all the votes
            total2016 += votes;
            districtToVoteMap2016.put(splitLine[0].toUpperCase(), votes);
        }
        turnout2016FileReader.close();

        int total2014 = 0;
        BufferedReader turnout2014FileReader = new BufferedReader(new FileReader(congressionalTurnout2014));
        //Clear header
        turnout2014FileReader.readLine();
        Map<String, Integer> districtToVoteMap2014 = new HashMap<>();
        while ((line = turnout2014FileReader.readLine()) != null) {
            String[] splitLine = line.split(",");
            int demVote = Integer.parseInt(splitLine[1]);
            int repVote = Integer.parseInt(splitLine[2]);

            //We count every vote for the 2014 total, even if they weren't contested.
            total2014 += demVote + repVote;

            String name = splitLine[0].toUpperCase();
            //Only put votes in the 2014 map if the district was contested.
            if (demVote != 0 && repVote != 0 && !redistrict2018.contains(name.substring(0, 2))
                    && !redistrict2016.contains(name.substring(0, 2))) {
                districtToVoteMap2014.put(name, demVote + repVote);
            }
        }
        turnout2014FileReader.close();

        //Average all contested districts.
        int average2014 = 0;
        for (String district : districtToVoteMap2014.keySet()) {
            average2014 += districtToVoteMap2014.get(district);
        }
        //This is some dumb stuff we have to do because of how java converts int and double back and forth.
        average2014 = (int) Math.round(((double) average2014) / districtToVoteMap2014.size());

        districtToVoteMap = new HashMap<>();
        for (String district : districtToVoteMap2016.keySet()) {
            if (redistrict2018.contains(district.substring(0, 2)) ||
                    (districtToVoteMap2016.get(district) == 0 && (!districtToVoteMap2014.containsKey(district)))) {
                //If it was redistricted for 2018 or it was uncontested in 2016 with no 2014 data, just fill in the
                // 2014 average.
                districtToVoteMap.put(district, average2014);
            } else if (!districtToVoteMap2014.containsKey(district)) {
                //If we have 2016 data but not 2014 data, scale the 2016 number to the 2014 total turnout and use that.
                districtToVoteMap.put(district,
                        (int) (Math.round(((double) total2014) / total2016 * districtToVoteMap2016.get(district))));
            } else {
                //If we have 2014 data, use it.
                districtToVoteMap.put(district, districtToVoteMap2014.get(district));
            }
        }
    }

    /**
     * Calculate the national shift.
     *
     * @param districts         A list of every district, with the fundamental dem win percent filled in.
     * @param genericDemPercent The democratic share of the two-party vote on the national generic ballot, from 0 to 1.
     * @return The national shift to apply to each district, where 0.01 is 1% in favor of the democrats and -0.01 is 1%
     * for the republicans.
     */
    @Override
    public double calcNationalShift(District[] districts, double genericDemPercent) {
        double numerator = 0;
        double denominator = 0;
        //Find the total number of democrat votes we expect and total number of dem votes we expect
        for (District district : districts) {
            numerator += districtToVoteMap.get(district.getName()) * district.getFundamentalDemPercent();
            denominator += districtToVoteMap.get(district.getName());
        }
        //Return the difference between the generic ballot and our predicted national percent of dem votes.
        return genericDemPercent - numerator / denominator;
    }
}
