import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Poll {

    public static final LocalDate ELECTION_DATE = LocalDate.of(2018, 11, 6);

    private final LocalDate dateTaken;
    private final double demPercent;
    private final int sampleSize;
    private final boolean registeredVoter;
    private final double houseLean;
    private final char grade;
    private final long daysBeforeElection;

    public Poll(LocalDate dateTaken, double demPercent, int sampleSize, boolean registeredVoter, double houseLean, char grade) {
        this.dateTaken = dateTaken;
        this.demPercent = demPercent;
        this.sampleSize = sampleSize;
        this.registeredVoter = registeredVoter;
        this.houseLean = houseLean;
        this.grade = grade;
        this.daysBeforeElection = ChronoUnit.DAYS.between(dateTaken, ELECTION_DATE);
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

    public char getGrade(){
        return grade;
    }
}
