import java.util.Map;

public class LogisticPollCalculator extends PollCalculator{

    private final Map<Character, Double> gradeQualityPoints;
    private final double daysCoefficient;
    private final double maxPollWeight;
    private final double logisiticShift;
    private final double logisiticSteepness;

    public LogisticPollCalculator(PollAverager pollAverager) {
        super(pollAverager);
    }

    @Override
    public double calculatePolls(District district) {
        if (district.hasPolls()) {
            double average = pollAverager.getAverage(district.getPolls());
            double x = 0;
            for (Poll poll : district.getPolls()){
                x += (Math.exp(-1*daysCoefficient*))*gradeQualityPoints.get(poll.getGrade());
            }
        }
        return 0;
    }
}
