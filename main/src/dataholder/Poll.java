package dataholder;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * A single national or district-level poll.
 */
public class Poll {

    /**
     * The date of the 2018 US House election.
     */
    public static final LocalDate ELECTION_DATE = LocalDate.of(2018, 11, 6);
    /**
     * The last date this poll was taken.
     */
    private final LocalDate dateTaken;
    /**
     * The percent of respondents that said they would vote for the Democrat.
     */
    private final double rawDemPercent;
    /**
     * The percent of respondents that said they would vote for the Republican.
     */
    private final double rawRepPercent;
    /**
     * How many people were polled. Some polls give a fractional sample size for some reason, so this is a double.
     */
    private final double sampleSize;
    /**
     * The voter model used in this poll.
     */
    private final VoterModel voterModel;
    /**
     * The corrective factor to apply to correct for the pollster's bias.
     */
    private final double houseLean;
    /**
     * The pollster's 538 grade.
     */
    private final Grade grade;
    /**
     * The pollster's name.
     */
    private final String pollsterName;
    /**
     * How many days before the election this poll was taken.
     */
    private final long daysBeforeElection;
    /**
     * The percent of the two-party vote the Democrats will get, according to this poll.
     */
    private final double demPercent;
    /**
     * The standard deviation of this poll, according to just the sample size..
     */
    private final double theoreticalStandardDeviation;
    /**
     * The adjusted standard deviation of this poll.
     */
    private double standardDeviation;

    /**
     * Default constructor.
     *
     * @param dateTaken     The last date this poll was taken.
     * @param rawDemPercent The percent of respondents that said they would vote for the Democrat.
     * @param rawRepPercent The percent of respondents that said they would vote for the Republican.
     * @param sampleSize    How many people were polled. Some polls give a fractional sample size for some reason, so
     *                      this is a double.
     * @param voterModel    The voter model used in this poll.
     * @param houseLean     The corrective factor to apply to correct for the pollster's bias.
     * @param grade         The pollster's 538 grade.
     * @param pollsterName  The pollster's name.
     */
    public Poll(LocalDate dateTaken, double rawDemPercent, double rawRepPercent, double sampleSize,
                VoterModel voterModel,
                double houseLean, Grade grade, String pollsterName) {
        this.dateTaken = dateTaken;
        this.rawDemPercent = rawDemPercent;
        this.rawRepPercent = rawRepPercent;
        this.demPercent = rawDemPercent / (rawRepPercent + rawDemPercent);
        this.sampleSize = sampleSize;
        this.voterModel = voterModel;
        this.houseLean = houseLean;
        this.grade = grade;
        this.daysBeforeElection = ChronoUnit.DAYS.between(dateTaken, ELECTION_DATE);
        this.pollsterName = pollsterName;
        this.theoreticalStandardDeviation = Math.sqrt(demPercent * (1 - demPercent) / sampleSize);
        //Start with adjusted the same as theoretical
        this.standardDeviation = this.theoreticalStandardDeviation;
    }

    /**
     * @return The last date this poll was taken.
     */
    public LocalDate getDateTaken() {
        return dateTaken;
    }

    /**
     * @return How many days before the election this poll was taken.
     */
    public long getDaysBeforeElection() {
        return daysBeforeElection;
    }

    /**
     * @return The percent of the two-party vote the Democrats will get, according to this poll.
     */
    public double getDemPercent() {
        return demPercent;
    }

    /**
     * @return How many people were polled. Some polls give a fractional sample size for some reason, so this is a
     * double.
     */
    public double getSampleSize() {
        return sampleSize;
    }

    /**
     * @return The voter model used in this poll.
     */
    public VoterModel getVoterModel() {
        return voterModel;
    }

    /**
     * @return The corrective factor to apply to correct for the pollster's bias.
     */
    public double getHouseLean() {
        return houseLean;
    }

    /**
     * @return The pollster's 538 grade.
     */
    public Grade getGrade() {
        return grade;
    }

    /**
     * @return The percent of respondents that said they would vote for the Democrat.
     */
    public double getRawDemPercent() {
        return rawDemPercent;
    }

    /**
     * @return The percent of respondents that said they would vote for the Republican.
     */
    public double getRawRepPercent() {
        return rawRepPercent;
    }

    /**
     * @return The pollster's name.
     */
    public String getPollsterName() {
        return pollsterName;
    }

    /**
     * @return The standard deviation of this poll, according to just the sample size.
     */
    public double getTheoreticalStandardDeviation() {
        return theoreticalStandardDeviation;
    }

    /**
     * @return The adjusted standard deviation of this poll.
     */
    public double getStandardDeviation() {
        return standardDeviation;
    }

    /**
     * @param standardDeviation The adjusted standard deviation of this poll.
     */
    public void setStandardDeviation(double standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

    /**
     * An enum for who a poll includes.
     */
    public enum VoterModel {
        //All registered voters
        REGISTERED,
        //Only registered voters who are probably going to vote in this election
        LIKELY,
        //IDK what this means but 538 has it as something separate so....
        VOTERS,
        //All adults
        ADULTS;

        /**
         * Parse a VoterModel object from a string. Not case-sensitive.
         *
         * @param string A string representing a voter model.
         * @return the corresponding VoterModel, or null if the string isn't recognized.
         */
        public static VoterModel parseFromString(String string) {
            switch (string.toLowerCase()) {
                case "registered":
                    return REGISTERED;
                case "likely":
                    return LIKELY;
                case "voters":
                    return VOTERS;
                case "adults":
                    return ADULTS;
            }
            return null;
        }
    }
}
