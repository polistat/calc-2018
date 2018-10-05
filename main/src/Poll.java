import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Poll {

    public static final LocalDate ELECTION_DATE = LocalDate.of(2018, 11, 6);

    private final LocalDate dateTaken;
    private final double rawDemPercent;
    private final double rawRepPercent;
    private final int sampleSize;
    private final boolean registeredVoter;
    private final double houseLean;
    private final Grade grade;
    private final String pollsterName;
    private final long daysBeforeElection;

    private final double demPercent;
    private final double standardDeviation;

    public Poll(LocalDate dateTaken, double rawDemPercent, double rawRepPercent, int sampleSize, boolean registeredVoter,
                double houseLean, Grade grade, String pollsterName) {
        this.dateTaken = dateTaken;
        this.rawDemPercent = rawDemPercent;
        this.rawRepPercent = rawRepPercent;
        this.demPercent = rawDemPercent/(rawRepPercent+rawDemPercent);
        this.sampleSize = sampleSize;
        this.registeredVoter = registeredVoter;
        this.houseLean = houseLean;
        this.grade = grade;
        this.daysBeforeElection = ChronoUnit.DAYS.between(dateTaken, ELECTION_DATE);
        this.pollsterName = pollsterName;
        this.standardDeviation = Math.sqrt(demPercent*(1-demPercent)/sampleSize);
    }

    public LocalDate getDateTaken() {
        return dateTaken;
    }

    public long getDaysBeforeElection(){
        return daysBeforeElection;
    }

    public double getDemPercent() {
        return demPercent;
    }

    public int getSampleSize() {
        return sampleSize;
    }

    public boolean isRegisteredVoter() {
        return registeredVoter;
    }

    public double getHouseLean() {
        return houseLean;
    }

    public Grade getGrade(){
        return grade;
    }

    public double getRawDemPercent() {
        return rawDemPercent;
    }

    public double getRawRepPercent() {
        return rawRepPercent;
    }

    public String getPollsterName() {
        return pollsterName;
    }

    public double getStandardDeviation(){
        return standardDeviation;
    }

    public static Poll[] readNationalPolls(String filename) throws IOException {
        String line;
        List<Poll> polls = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        //Clear header line
        reader.readLine();
        while ((line = reader.readLine()) != null){
            String[] commaSplit = line.split(",");
            LocalDate date = LocalDate.parse(commaSplit[0], DateTimeFormatter.ofPattern("M/d/yyyy"));
            double rawDemPercent = Double.parseDouble(commaSplit[1]);
            double rawRepPercent = Double.parseDouble(commaSplit[2]);
            int sampleSize = Integer.parseInt(commaSplit[3]);
            boolean registeredVoter = Boolean.parseBoolean(commaSplit[4]);
            double houseLean = Double.parseDouble(commaSplit[5]);
            Grade grade = Grade.parseGrade(commaSplit[6]);
            String pollsterName = commaSplit[7];
            polls.add(new Poll(date, rawDemPercent, rawRepPercent, sampleSize, registeredVoter, houseLean, grade, pollsterName));
        }
        reader.close();

        return polls.toArray(new Poll[1]);
    }
}
