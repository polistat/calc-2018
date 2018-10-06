import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DZhuNatlShiftCalc implements NationalShiftCalculator {

    private final  Map<String, Integer> districtToVoteMap;

    public DZhuNatlShiftCalc(String congressionalTurnout2014, String congressionalTurnout2016, Set<String> redistrict2018, Set<String> redistrict2016) throws IOException {
        String line;

        int total2016 = 0;
        BufferedReader turnout2016FileReader =  new BufferedReader(new FileReader(congressionalTurnout2016));
        //Clear header
        turnout2016FileReader.readLine();
        Map<String, Integer> districtToVoteMap2016 = new HashMap<>();
        while ((line = turnout2016FileReader.readLine()) != null){
            String[] splitLine = line.split(",");
            int votes = Integer.parseInt(splitLine[1]);
            total2016 += votes;
            districtToVoteMap2016.put(splitLine[0].toUpperCase(), votes);
        }
        turnout2016FileReader.close();

        int total2014 = 0;
        BufferedReader turnout2014FileReader =  new BufferedReader(new FileReader(congressionalTurnout2014));
        //Clear header
        turnout2014FileReader.readLine();
        Map<String, Integer> districtToVoteMap2014 = new HashMap<>();
        while ((line = turnout2014FileReader.readLine()) != null){
            String[] splitLine = line.split(",");
            int demVote = Integer.parseInt(splitLine[1]);
            int repVote = Integer.parseInt(splitLine[2]);
            total2014 += demVote + repVote;
            String name = splitLine[0].toUpperCase();
            if (demVote != 0 && repVote != 0 && !redistrict2018.contains(name.substring(0,2))
                    && !redistrict2016.contains(name.substring(0,2))){
                districtToVoteMap2014.put(name, demVote+repVote);
            }
        }
        turnout2014FileReader.close();

        int average2014 = 0;
        for (String district : districtToVoteMap2014.keySet()){
            average2014 += districtToVoteMap2014.get(district);
        }
        average2014 = (int) Math.round(((double) average2014)/districtToVoteMap2014.size());

        districtToVoteMap = new HashMap<>();
        for (String district : districtToVoteMap2016.keySet()){
            if (redistrict2018.contains(district.substring(0 ,2)) ||
                    (districtToVoteMap2016.get(district) == 0 && (!districtToVoteMap2014.containsKey(district)))){
                districtToVoteMap.put(district, average2014);
            } else if (!districtToVoteMap2014.containsKey(district)){
                districtToVoteMap.put(district, (int) (Math.round(((double) total2014)/total2016*districtToVoteMap2016.get(district))));
            } else {
                districtToVoteMap.put(district, districtToVoteMap2014.get(district));
            }
        }
    }

    @Override
    public double calcNationalShift(District[] districts, double genericDemPercent) throws IOException {
        double numerator = 0;
        double denominator = 0;
        for (District district : districts){
            numerator += districtToVoteMap.get(district.getName()) * district.getFundamentalDemPercent();
            denominator += districtToVoteMap.get(district.getName());
        }
        return genericDemPercent - numerator/denominator;
    }
}
