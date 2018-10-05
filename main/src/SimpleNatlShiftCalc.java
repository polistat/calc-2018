import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SimpleNatlShiftCalc implements NationalShiftCalculator {

    private final  Map<String, Integer> districtToVoteMap;

    public SimpleNatlShiftCalc(String lastCongressionalDataFile) throws IOException {
        String line;
        BufferedReader lastCongressionalFileReader =  new BufferedReader(new FileReader(lastCongressionalDataFile));
        //Clear header
        lastCongressionalFileReader.readLine();
        districtToVoteMap = new HashMap<>();
        while ((line = lastCongressionalFileReader.readLine()) != null){
            String[] splitLine = line.split(",");
            int demVote = Integer.parseInt(splitLine[1]);
            int repVote = Integer.parseInt(splitLine[2]);
            if (demVote != 0 && repVote != 0){
                districtToVoteMap.put(splitLine[0].toUpperCase(), demVote+repVote);
            }
        }
        lastCongressionalFileReader.close();
    }

    @Override
    public double calcNationalShift(District[] districts, double genericDemPercent) throws IOException {
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
