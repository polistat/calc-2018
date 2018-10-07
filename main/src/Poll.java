import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Poll {

    public static final LocalDate ELECTION_DATE = LocalDate.of(2018, 11, 6);

    private final LocalDate dateTaken;
    private final double rawDemPercent;
    private final double rawRepPercent;
    private final double sampleSize;
    private final boolean registeredVoter;
    private final double houseLean;
    private final Grade grade;
    private final String pollsterName;
    private final long daysBeforeElection;

    private final double demPercent;
    private final double standardDeviation;

    public Poll(LocalDate dateTaken, double rawDemPercent, double rawRepPercent, double sampleSize,
                boolean registeredVoter,
                double houseLean, Grade grade, String pollsterName) {
        this.dateTaken = dateTaken;
        this.rawDemPercent = rawDemPercent;
        this.rawRepPercent = rawRepPercent;
        this.demPercent = rawDemPercent / (rawRepPercent + rawDemPercent);
        this.sampleSize = sampleSize;
        this.registeredVoter = registeredVoter;
        this.houseLean = houseLean;
        this.grade = grade;
        this.daysBeforeElection = ChronoUnit.DAYS.between(dateTaken, ELECTION_DATE);
        this.pollsterName = pollsterName;
        this.standardDeviation = Math.sqrt(demPercent * (1 - demPercent) / sampleSize);
    }

    public LocalDate getDateTaken() {
        return dateTaken;
    }

    public long getDaysBeforeElection() {
        return daysBeforeElection;
    }

    public double getDemPercent() {
        return demPercent;
    }

    public double getSampleSize() {
        return sampleSize;
    }

    public boolean isRegisteredVoter() {
        return registeredVoter;
    }

    public double getHouseLean() {
        return houseLean;
    }

    public Grade getGrade() {
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

    public double getStandardDeviation() {
        return standardDeviation;
    }
}
