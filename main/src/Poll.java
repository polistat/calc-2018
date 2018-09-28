import java.util.Date;

public class Poll {

    private final Date dateTaken;
    private final double demMargin;
    private final int sampleSize;
    private final boolean registeredVoter;
    private final double houseLean;
    private final String grade;

    public Poll(Date dateTaken, double demMargin, int sampleSize, boolean registeredVoter, double houseLean, String grade) {
        this.dateTaken = dateTaken;
        this.demMargin = demMargin;
        this.sampleSize = sampleSize;
        this.registeredVoter = registeredVoter;
        this.houseLean = houseLean;
        this.grade = grade;
    }

    public Date getDateTaken() {
        return dateTaken;
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

    public String getGrade(){
        return grade;
    }
}
