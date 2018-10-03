import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class District {

    private final String name;
    private final Poll[] polls;
    private final boolean repIncumbent;
    private final boolean demIncumbent;
    private final double obama2012;
    private final Double dem2014;
    private final double hillary2016;
    private final Double dem2016;
    private final double elasticity;
    private final Double bantorMargin;

    private double fundamentalDemPercent;
    private double fundamentalStDv;
    private double genericCorrectedDemPercent;
    private double genericCorrectedStDv;
    private double finalDemPercent;
    private double finalStDv;

    private District(String name, Poll[] polls, boolean repIncumbent,
                    boolean demIncumbent, double obama2012, Double dem2014,
                    double hillary2016, Double dem2016, double elasticity,
                    Double bantorMargin) {
        this.name = name;
        this.polls = polls;
        this.repIncumbent = repIncumbent;
        this.demIncumbent = demIncumbent;
        this.obama2012 = obama2012;
        this.dem2014 = dem2014;
        this.hillary2016 = hillary2016;
        this.dem2016 = dem2016;
        this.elasticity = elasticity;
        this.bantorMargin = bantorMargin;
    }

    public String getName() {
        return name;
    }

    public Poll[] getPolls() {
        return polls;
    }

    public boolean isRepIncumbent() {
        return repIncumbent;
    }

    public boolean isDemIncumbent() {
        return demIncumbent;
    }

    public double getObama2012() {
        return obama2012;
    }

    public Double getDem2014() {
        return dem2014;
    }

    public double getHillary2016() {
        return hillary2016;
    }

    public Double getDem2016() {
        return dem2016;
    }

    public double getElasticity() {
        return elasticity;
    }

    public Double getBantorMargin() {
        return bantorMargin;
    }

    public double getFundamentalDemPercent() {
        return fundamentalDemPercent;
    }

    public void setFundamentalDemPercent(double fundamentalDemPercent) {
        this.fundamentalDemPercent = fundamentalDemPercent;
    }

    public double getGenericCorrectedDemPercent() {
        return genericCorrectedDemPercent;
    }

    public void setGenericCorrectedDemPercent(double genericCorrectedDemPercent) {
        this.genericCorrectedDemPercent = genericCorrectedDemPercent;
    }

    public double getFinalDemPercent() {
        return finalDemPercent;
    }

    public void setFinalDemPercent(double finalDemPercent) {
        this.finalDemPercent = finalDemPercent;
    }
    
    public double getFundamentalStDv() {
        return fundamentalStDv;
    }

    public void setFundamentalStDv(double fundamentalStDv) {
        this.fundamentalStDv = fundamentalStDv;
    }
    
    public double getGenericCorrectedStDv() {
        return genericCorrectedStDv;
    }

    public void setGenericCorrectedStDv(double genericCorrectedStDv) {
        this.genericCorrectedStDv = genericCorrectedStDv;
    }
    
    public double getFinalStDv() {
        return finalStDv;
    }

    public void setFinalStDv(double finalStDv) {
        this.finalStDv = finalStDv;
    }

    public boolean hasPolls(){
        return polls != null && !(polls.length == 0);
    }

    public static District[] parseFromCSV(String districtFile, String pollFile) throws IOException, ParseException {
        String line;
        BufferedReader pollFileReader = new BufferedReader(new FileReader(pollFile));
        //Clear header line
        pollFileReader.readLine();
        Map<String, List<Poll>> nameToPollMap = new HashMap<>();
        while ((line = pollFileReader.readLine()) != null){
            String[] commaSplit = line.split(",");
            String name = getName(commaSplit[0], commaSplit[1]);
            LocalDate date = LocalDate.parse(commaSplit[2], DateTimeFormatter.ofPattern("mm/dd/yy"));
            double demMargin = Double.parseDouble(commaSplit[3]);
            int sampleSize = Integer.parseInt(commaSplit[4]);
            boolean registeredVoter = Boolean.parseBoolean(commaSplit[5]);
            double houseLean = Double.parseDouble(commaSplit[6]);
            char vote = commaSplit[7].toLowerCase().charAt(0);
            Poll poll = new Poll(date, demMargin, sampleSize, registeredVoter, houseLean, vote);
            if (nameToPollMap.containsKey(name)){
                nameToPollMap.get(name).add(poll);
            } else {
                List<Poll> pollList = new ArrayList<>();
                pollList.add(poll);
                nameToPollMap.put(name, pollList);
            }
        }

        BufferedReader districtFileReader = new BufferedReader(new FileReader(districtFile));
        //Clear header line
        districtFileReader.readLine();
        List<District> toRet = new ArrayList<>();
        while ((line = districtFileReader.readLine()) != null){
            String[] commaSplit = line.split(",");
            String name = getName(commaSplit[0], commaSplit[1]);
            boolean repIncumbent = Integer.parseInt(commaSplit[2]) == 1;
            boolean demIncumbent = Integer.parseInt(commaSplit[3]) == 1;
            double obama2012 = Double.parseDouble(commaSplit[4]);
            Double dem2014 = Double.parseDouble(commaSplit[5]);
            double hillary2016 = Double.parseDouble(commaSplit[6]);
            Double dem2016 = Double.parseDouble(commaSplit[7]);
            double elastity = Double.parseDouble(commaSplit[8]);
            Double bantorMargin;
            if (commaSplit.length < 10 || commaSplit[9].isEmpty()) {
                bantorMargin = null;
            } else {
                bantorMargin = Double.parseDouble(commaSplit[9]);
            }
            Poll[] polls;
            if (nameToPollMap.containsKey(name)) {
                polls = (Poll[]) nameToPollMap.get(name).toArray();
            } else {
                polls = null;
            }
            if (dem2014 == 0 || dem2014 == 1){
                dem2014 = null;
            }
            if (dem2016 == 0 || dem2016 == 1){
                dem2016 = null;
            }
            toRet.add(new District(name, polls, repIncumbent, demIncumbent, obama2012, dem2014,
                    hillary2016, dem2016, elastity, bantorMargin));
        }
        return (District[]) toRet.toArray();
    }

    public static String getName(String state, String districtNum){
        return String.format("%s-%02d", state, Integer.parseInt(districtNum));
    }
}
