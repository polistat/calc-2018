import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Calculate the national shift using 2014 turnout data.
 */
public class SimpleNatlShiftCalc implements NationalShiftCalculator {

    /**
     * Map of district names to how many people voted in that district in 2014 for districts that were contested in
     * 2014.
     */
    private final Map<String, Integer> districtToVoteMap;

    /**
     * Default constructor.
     *
     * @param congressionalTurnout2014 he file with the turnout for the 2014 congressional elections.
     * @throws IOException If the turnout file is missing/improperly formatted.
     */
    public SimpleNatlShiftCalc(String congressionalTurnout2014) throws IOException {
        //Define line up here to avoid garbage collection
        String line;

        BufferedReader lastCongressionalFileReader = new BufferedReader(new FileReader(congressionalTurnout2014));
        //Clear header
        lastCongressionalFileReader.readLine();
        districtToVoteMap = new HashMap<>();
        while ((line = lastCongressionalFileReader.readLine()) != null) {
            String[] splitLine = line.split(",");
            int demVote = Integer.parseInt(splitLine[1]);
            int repVote = Integer.parseInt(splitLine[2]);
            //Only count contested districts
            if (demVote != 0 && repVote != 0) {
                districtToVoteMap.put(splitLine[0].toUpperCase(), demVote + repVote);
            }
        }
        lastCongressionalFileReader.close();
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
        //Find the total number of democrat votes we expect and total number of dem votes we expect, in districts
        // where 2014 was contested.
        for (District district : districts) {
            if (districtToVoteMap.containsKey(district.getName())) {
                numerator += districtToVoteMap.get(district.getName()) * district.getFundamentalDemPercent();
                denominator += districtToVoteMap.get(district.getName());
            }
        }
        //Return the difference between the generic ballot and our predicted national percent of dem votes.
        return genericDemPercent - numerator / denominator;
    }
}
