import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

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
     * The percent of the two-party vote the democrats will get, according to this poll.
     */
    private final double demPercent;
    /**
     * The standard deviation of this poll.
     */
    private final double standardDeviation;

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
        this.standardDeviation = Math.sqrt(demPercent * (1 - demPercent) / sampleSize);
    }

    /**
     * Read a set of national generic ballot polls from a file.
     *
     * @param filename The file with the poll data.
     * @return A list of polls corresponding to the data in the file.
     * @throws IOException If the file is missing or improperly formatted
     */
    public static Poll[] readNationalPolls(String filename) throws IOException {
        //Define line out here to avoid garbage collection.
        String line;
        List<Poll> polls = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        //Clear header line
        reader.readLine();
        while ((line = reader.readLine()) != null) {
            String[] commaSplit = line.split(",");
            //File must be formatted as follows:
            //final date the poll was taken,dem percent,rep percent,sample size,which voter model was used,pollster
            // lean,pollster grade,pollster name
            LocalDate date = LocalDate.parse(commaSplit[0], DateTimeFormatter.ofPattern("M/d/yyyy"));
            double rawDemPercent = Double.parseDouble(commaSplit[1]);
            double rawRepPercent = Double.parseDouble(commaSplit[2]);
            double sampleSize = Double.parseDouble(commaSplit[3]);
            VoterModel voterModel = VoterModel.parseFromString(commaSplit[4]);
            double houseLean = Double.parseDouble(commaSplit[5]);
            Grade grade = Grade.parseGrade(commaSplit[6]);
            String pollsterName = commaSplit[7];
            polls.add(new Poll(date, rawDemPercent, rawRepPercent, sampleSize, voterModel, houseLean, grade,
                    pollsterName));
        }
        reader.close();

        return polls.toArray(new Poll[1]);
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
     * @return The percent of the two-party vote the democrats will get, according to this poll.
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
     * @return The standard deviation of this poll.
     */
    public double getStandardDeviation() {
        return standardDeviation;
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
