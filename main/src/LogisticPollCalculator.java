import java.util.Map;

public class LogisticPollCalculator extends PollCalculator{

    private final Map<Character, Double> gradeQualityPoints;
    private final double daysCoefficient;
    private final double maxPollWeight;
    private final double logisticShift;
    private final double logisticSteepness;
    private final double bantorWeight;
    private final double bantorStDv;

    public LogisticPollCalculator(PollAverager pollAverager, Map<Character, Double> gradeQualityPoints,
                                  double daysCoefficient, double maxPollWeight, double logisticShift,
                                  double logisticSteepness, double bantorWeight, double bantorStDv) {
        super(pollAverager);
        this.gradeQualityPoints = gradeQualityPoints;
        this.daysCoefficient = daysCoefficient;
        this.maxPollWeight = maxPollWeight;
        this.logisticShift = logisticShift;
        this.logisticSteepness = logisticSteepness;
        this.bantorWeight = bantorWeight;
        this.bantorStDv = bantorStDv;
    }

    @Override
    public double calculatePolls(District district) {
        double pollAverage;
        double pollStdv;
        double pollWeight;
        if (district.hasPolls()) {
            pollAverage = pollAverager.getAverage(district.getPolls());
            pollStdv = pollAverager.getAverage(district.getPolls());
            double x = 0;
            for (Poll poll : district.getPolls()){
                x += (Math.exp(-1*daysCoefficient*poll.getDaysBeforeElection()))*gradeQualityPoints.get(poll.getGrade());
            }
            pollWeight = maxPollWeight/(1+Math.exp(-logisticSteepness *(x- logisticShift)));
        } else {
            pollAverage = district.getBantorMargin();
            pollStdv = bantorStDv;
            pollWeight = bantorWeight;
        }
        district.setFinalDemPercent(pollWeight*pollAverage + (1-pollWeight)*district.getGenericCorrectedDemPercent());
        district.setFinalStDv(Math.sqrt(Math.pow(pollStdv, 2)*pollWeight + Math.pow(district.getGenericCorrectedStDv(), 2)*(1-pollWeight)));
        return district.getFinalDemPercent();
    }
}
