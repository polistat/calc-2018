import java.util.Map;

/**
 * Finds the average democratic vote percent and standard deviation for a list of polls.
 */
public interface PollAverager {

    /**
     * Find the average democratic vote percent for a list of polls.
     *
     * @param polls The polls to average.
     * @return The average dem vote percent, from 0 to 1.
     */
    double getAverage(Poll[] polls);

    /**
     * Find the standard deviation of the democratic vote percent for a list of polls.
     *
     * @param polls The polls to find the standard deviation of.
     * @return The standard deviation, where 0.01 is 1%.
     */
    double getStDv(Poll[] polls);
    double getStDv(Poll[] polls, Map<Grade, Double> gradeQualityPoints);

}
