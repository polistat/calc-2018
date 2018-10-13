package auspice;

import dataholder.Poll;

/**
 * Averages polls using a weighting function that's an exponential decay over time.
 */
public class ExponentialPollAverager implements PollAverager {

    /**
     * The positive coefficient that gets multiplies by the poll's age, then plugged into e^-x.
     */
    private final double exponentialCoefficient;

    /**
     * Default constructor.
     *
     * @param exponentialCoefficient The positive coefficient that gets multiplies by the poll's age, then plugged into
     *                               e^-x. Should be less than 1.
     */
    public ExponentialPollAverager(double exponentialCoefficient) {
        this.exponentialCoefficient = exponentialCoefficient;
    }

    /**
     * Find the average Democratic vote percent for a list of polls.
     *
     * @param polls The polls to average.
     * @return The average dem vote percent, from 0 to 1.
     */
    @Override
    public double getAverage(Poll[] polls) {
        double numerator = 0;
        double denominator = 0;
        //Sum each poll's dem percent*weight and the weights.
        for (Poll poll : polls) {
            numerator += poll.getDemPercent() * Math.exp(-exponentialCoefficient * poll.getDaysBeforeElection());
            denominator += Math.exp(-exponentialCoefficient * poll.getDaysBeforeElection());
        }
        return numerator / denominator;
    }

    /**
     * Find the standard deviation of the Democratic vote percent for a list of polls.
     *
     * @param polls The polls to find the standard deviation of.
     * @return The standard deviation, where 0.01 is 1%.
     */
    @Override
    public double getStDv(Poll[] polls) {
        //Sum all of the weights
        double weightSum = 0;
        for (Poll poll : polls) {
            weightSum += Math.exp(-exponentialCoefficient * poll.getDaysBeforeElection());
        }

        double variance = 0;
        //Find the variance as the sum of the (normalized weight * poll SD)^2
        for (Poll poll : polls) {
            variance += Math.pow(Math.exp(-exponentialCoefficient * poll.getDaysBeforeElection()) / weightSum * poll.getStandardDeviation(), 2);
        }
        //Take the square root of the variance to get the standard deviation.
        return Math.sqrt(variance);
    }
}
