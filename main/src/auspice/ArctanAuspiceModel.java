package auspice;

import dataholder.District;
import dataholder.Grade;
import dataholder.Poll;

import java.util.Map;

/**
 * Predict Democratic votes percent using district-level polls or Blairvoyance. The weight of the polls versus the
 * shifted fundamentals is an arctan function.
 */
public class ArctanAuspiceModel extends AuspiceModel {

    /**
     * A map of each 538 pollster grade to the "quality point" score associated with that grade.
     */
    private final Map<Grade, Double> gradeQualityPoints;

    /**
     * The (positive) number to multiply by the number of days a poll was taken before the election, then plug into e^-x
     * to get the age adjustment for poll weighting. Should be less than 1.
     */
    private final double daysCoefficient;

    /**
     * The maximum weight of the polls compared to the shifted fundamentals, from 0 to 1.
     */
    private final double maxPollWeight;

    /**
     * How much to shift the arctan function by in x.
     */
    private final double arctanShift;

    /**
     * What the derivative of the arctan function should be at the center, when x = arctanShift.
     */
    private final double arctanSteepness;

    /**
     * The weight to give to Blairvoyance relative to the shifted fundamentals in districts with no polls, from 0 to 1.
     */
    private final double blairvoyanceWeight;

    /**
     * The standard deviation of the Blairvoyance predicted Democrat percent.
     */
    private final double blairvoyanceStDv;

    private final BlairvoyanceWeightCalculator bvCalc;

    /**
     * Default constructor.
     *
     * @param pollAverager       The method to use for averaging polls when a district has multiple polls.
     * @param gradeQualityPoints A map of each 538 pollster grade to the "quality point" score associated with that
     *                           grade.
     * @param daysCoefficient    The (positive) number to multiply by the number of days a poll was taken before the
     *                           election, then plug into e^-x to get the age adjustment for poll weighting. Should be
     *                           less than 1.
     * @param maxPollWeight      The maximum weight of the polls compared to the shifted fundamentals, from 0 to 1.
     * @param arctanShift        How much to shift the arctan function by in x.
     * @param arctanSteepness    What the derivative of the arctan function should be at the center, when x =
     *                           arctanShift.
     * @param blairvoyanceWeight The weight to give to Blairvoyance relative to the shifted fundamentals in districts
     *                           with no polls, from 0 to 1.
     * @param blairvoyanceStDv   The standard deviation of the Blairvoyance predicted Democrat percent.
     */
    public ArctanAuspiceModel(PollAverager pollAverager, BlairvoyanceWeightCalculator bvCalc,
                              Map<Grade, Double> gradeQualityPoints,
                              double daysCoefficient, double maxPollWeight, double arctanShift,
                              double arctanSteepness, double blairvoyanceWeight, double blairvoyanceStDv) {
        super(pollAverager);
        this.gradeQualityPoints = gradeQualityPoints;
        this.daysCoefficient = daysCoefficient;
        this.maxPollWeight = maxPollWeight;
        this.arctanShift = arctanShift;
        this.arctanSteepness = arctanSteepness;
        this.blairvoyanceWeight = blairvoyanceWeight;
        this.blairvoyanceStDv = blairvoyanceStDv;
        this.bvCalc = bvCalc;
    }

    /**
     * Calculate Democratic vote percent and standard deviation using the polls in that district or Blairvoyance. This
     * modifies the district's auspiceDemPercent and auspiceDemStDv.
     *
     * @param district A district with the bigmood dem percent and standard deviation already calculated.
     */
    @Override
    public void calculateAuspice(District district) {
        double pollAverage;
        double pollStDv;
        double pollWeight;
        if (district.hasPolls()) {
            pollAverage = pollAverager.getAverage(district.getPolls());
            pollStDv = pollAverager.getStDv(district.getPolls());
            double x = 0;
            //Sum of qualityPoints*e^(-daysCoefficient*t)
            for (Poll poll : district.getPolls()) {
                x += (Math.exp(-daysCoefficient * poll.getDaysBeforeElection())) * gradeQualityPoints.get(poll.getGrade());
            }
            //Plug into arctan, with a coefficient 2/pi outside to make the function have a range of 0, maxPollWeight.
            pollWeight = maxPollWeight * (2. / Math.PI) * (Math.atan(arctanSteepness * (x - arctanShift)));
        } else {
            //If there's no polls, just use Blairvoyance.
            pollAverage = district.getBlairvoyanceDemPercent();
            pollStDv = blairvoyanceStDv;
            pollWeight = blairvoyanceWeight * bvCalc.getBlairvoyanceWeight(district);
        }

        //Weights are normalized already so we don't need to divide or anything
        district.setAuspiceDemPercent(pollWeight * pollAverage + (1 - pollWeight) * district.getBigmoodDemPercent());

        //Pythagorean theorem of statistics
        district.setAuspiceStDv(Math.sqrt(Math.pow(pollStDv * pollWeight, 2) + Math.pow(district.getBigmoodStDv() * (1 - pollWeight), 2)));
    }
}
