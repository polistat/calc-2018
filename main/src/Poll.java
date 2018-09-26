import java.util.Date;

public class Poll {

    private final Date dateTaken;
    private final double demMargin;
    private final int sampleSize;
    private final boolean registeredVoter;

    public Poll(Date dateTaken, double demMargin, int sampleSize, boolean registeredVoter) {
        this.dateTaken = dateTaken;
        this.demMargin = demMargin;
        this.sampleSize = sampleSize;
        this.registeredVoter = registeredVoter;
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
}
