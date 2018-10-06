public class ExponentialPollAverager implements PollAverager {

    private final double exponentialCoefficient;

    public ExponentialPollAverager(double exponentialCoefficient) {
        this.exponentialCoefficient = exponentialCoefficient;
    }

    @Override
    public double getAverage(Poll[] polls) {
        double numerator = 0;
        double denominator = 0;
        for (Poll poll : polls) {
            numerator += poll.getDemPercent() * Math.exp(-exponentialCoefficient * poll.getDaysBeforeElection());
            denominator += Math.exp(-exponentialCoefficient * poll.getDaysBeforeElection());
        }
        return numerator / denominator;
    }

    @Override
    public double getStDv(Poll[] polls) {
        double weightSum = 0;
        for (Poll poll : polls) {
            weightSum += Math.exp(-exponentialCoefficient * poll.getDaysBeforeElection());
        }
        double variance = 0;
        for (Poll poll : polls) {
            variance += Math.pow(Math.exp(-exponentialCoefficient * poll.getDaysBeforeElection()) * poll.getStandardDeviation() / weightSum, 2);
        }
        return Math.sqrt(variance);
    }
}
