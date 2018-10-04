import java.util.Map;

public class ArctanPollCalculator extends PollCalculator{

    private final Map<Grade, Double> gradeQualityPoints;
    private final double daysCoefficient;
    private final double maxPollWeight;
    private final double arctanShift;
    private final double arctanSteepness;
    private final double bantorWeight;
    private final double bantorStDv;

    public ArctanPollCalculator(PollAverager pollAverager, Map<Grade, Double> gradeQualityPoints,
                                double daysCoefficient, double maxPollWeight, double arctanShift,
                                double arctanSteepness, double bantorWeight, double bantorStDv) {
        super(pollAverager);
        this.gradeQualityPoints = gradeQualityPoints;
        this.daysCoefficient = daysCoefficient;
        this.maxPollWeight = maxPollWeight;
        this.arctanShift = arctanShift;
        this.arctanSteepness = arctanSteepness;
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
            pollWeight = maxPollWeight*(2./Math.PI)*(Math.atan(arctanSteepness * (x - arctanShift)));
        } else {
            pollAverage = district.getBantorDemPercent();
            pollStdv = bantorStDv;
            pollWeight = bantorWeight;
        }
        district.setFinalDemPercent(pollWeight*pollAverage + (1-pollWeight)*district.getGenericCorrectedDemPercent());
        district.setFinalStDv(Math.sqrt(Math.pow(pollStdv, 2)*pollWeight + Math.pow(district.getGenericCorrectedStDv(), 2)*(1-pollWeight)));
        return district.getFinalDemPercent();
    }
}
