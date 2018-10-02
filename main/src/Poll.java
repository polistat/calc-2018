import java.util.Date;
import java.util.GregorianCalendar;

public class Poll {

    public static final GregorianCalendar ELECTION_DATE = new GregorianCalendar(2018, 10, 6);

    private final GregorianCalendar dateTaken;
    private final double demMargin;
    private final int sampleSize;
    private final boolean registeredVoter;
    private final double houseLean;
    private final char grade;
    private final int daysBeforeElection;

    public Poll(GregorianCalendar dateTaken, double demMargin, int sampleSize, boolean registeredVoter, double houseLean, char grade) {
        this.dateTaken = dateTaken;
        this.demMargin = demMargin;
        this.sampleSize = sampleSize;
        this.registeredVoter = registeredVoter;
        this.houseLean = houseLean;
        this.grade = grade;
        this.daysBeforeElection = dateTaken.get
    }

    public Date getDateTaken() {
        return dateTaken;
    }

    public int getDaysBeforeElection(){

    }

    public double getDemMargin() {
        return demMargin;
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
