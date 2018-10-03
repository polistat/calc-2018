import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SimpleNatlShiftCalc implements NationalShiftCalculator {

    @Override
    public double calcNationalShift(String lastCongressionalDataFile, District[] districts, double genericDemPercent) throws IOException {
        String line;
        BufferedReader lastCongressionalFileReader =  new BufferedReader(new FileReader(lastCongressionalDataFile));
        Map<String, Integer> districtToVoteMap = new HashMap<>();
        while ((line = lastCongressionalFileReader.readLine()) != null){
            String[] splitLine = line.split(",");
            int demVote = Integer.parseInt(splitLine[2]);
            int repVote = Integer.parseInt(splitLine[3]);
            if (demVote != 0 && repVote != 0){
                districtToVoteMap.put(District.getName(splitLine[0], splitLine[1]), demVote+repVote);
            }
        }
        double numerator = 0;
        double denominator = 0;
        for (District district : districts){
            if (districtToVoteMap.containsKey(district.getName())){
                numerator += districtToVoteMap.get(district.getName())*district.getFundamentalDemPercent();
                denominator += districtToVoteMap.get(district.getName());
            }
        }
        return genericDemPercent - numerator/denominator;
    }
}
