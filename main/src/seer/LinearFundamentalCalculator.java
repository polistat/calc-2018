package seer;

import dataholder.District;

/**
 * Calculates the fundamentals as a linear function of Blair Partisan Index (BPI) and incumbency. BPI is itself a linear
 * function of obama margin, hillary margin, and democrat 2014 & 2016 margins.
 */
public class LinearFundamentalCalculator extends FundamentalCalculator {

    /**
     * The weight to give Obama's 2012 margin when calculating BPI.
     */
    private final double obama2012weight;

    /**
     * The weight to give the 2014 Democrat house candidate's margin when calculating BPI.
     */
    private final double dem2014weight;

    /**
     * The weight to give Hillary's 2012 margin when calculating BPI.
     */
    private final double hillary2016weight;

    /**
     * The weight to give the 2016 Democrat house candidate's margin when calculating BPI.
     */
    private final double dem2016weight;

    /**
     * The weight to give BPI when calculating fundamental margin.
     */
    private final double partisanshipWeight;

    /**
     * The weight to give democrat incumbency when calculating fundamental margin.
     */
    private final double demIncumbentWeight;

    /**
     * The weight to give republican incumbency when calculating fundamental margin.
     */
    private final double repIncumbentWeight;

    /**
     * The standard deviation of the fundamental MARGIN in elections with an incumbent.
     */
    private final double incumbentStDv;

    /**
     * The standard deviation of the fundamental MARGIN in elections without an incumbent or with 2 incumbents.
     */
    private final double openStDv;

    /**
     * Default constructor.
     *
     * @param obama2012weight    The weight to give Obama's 2012 margin when calculating BPI. BPI weights will be
     *                           normalized.
     * @param dem2014weight      The weight to give the 2014 Democrat house candidate's margin when calculating BPI. BPI
     *                           weights will be normalized.
     * @param hillary2016weight  The weight to give Hillary's 2012 margin when calculating BPI.BPI weights will be
     *                           normalized.
     * @param dem2016weight      The weight to give the 2016 Democrat house candidate's margin when calculating BPI. BPI
     *                           weights will be normalized.
     * @param partisanshipWeight The weight to give BPI when calculating fundamental margin. BPI weights will be
     *                           normalized.
     * @param demIncumbentWeight The weight to give democrat incumbency when calculating fundamental margin.
     * @param repIncumbentWeight The weight to give republican incumbency when calculating fundamental margin.
     * @param incumbentStDv      The standard deviation of the fundamental MARGIN in elections with one incumbent.
     * @param openStDv           The standard deviation of the fundamental MARGIN in elections without an incumbent or
     *                           with 2 incumbents.
     */
    public LinearFundamentalCalculator(double obama2012weight, double dem2014weight, double hillary2016weight,
                                       double dem2016weight, double partisanshipWeight, double demIncumbentWeight,
                                       double repIncumbentWeight, double incumbentStDv, double openStDv) {
        this.obama2012weight = obama2012weight;
        this.dem2014weight = dem2014weight;
        this.hillary2016weight = hillary2016weight;
        this.dem2016weight = dem2016weight;
        this.partisanshipWeight = partisanshipWeight;
        this.demIncumbentWeight = demIncumbentWeight;
        this.repIncumbentWeight = repIncumbentWeight;
        this.incumbentStDv = incumbentStDv;
        this.openStDv = openStDv;
    }

    /**
     * Calculate the fundamental percent of the vote the democratic candidate will get for a given district.
     *
     * @param district The district to calculate the fundamental vote percent for.
     * @return The fundamental democratic vote percent, from 0 to 1.
     */
    @Override
    public double calcFundamentalDemPercent(District district) {
        //Do the sum of the data points we have over the sum of the weights used.
        double numerator = obama2012weight * district.getObama2012() + hillary2016weight * district.getHillary2016();
        double denominator = obama2012weight + hillary2016weight;

        //If 2014 was uncontested, don't use it in the numerator or include its weight in the denominator.
        if (district.getDem2014() != null) {
            numerator += dem2014weight * (district.getDem2014()
                    - district.getDemInc14() * demIncumbentWeight / 2
                    - district.getRepInc14() * repIncumbentWeight / 2);
            denominator += dem2014weight;
        }

        //Same for 2016.
        if (district.getDem2016() != null) {
            numerator += dem2016weight * (district.getDem2016()
                    - district.getDemInc16() * demIncumbentWeight / 2
                    - district.getRepInc16() * repIncumbentWeight / 2);
            denominator += dem2016weight;
        }

        double BPI = numerator / denominator;
        double predictedDemMargin = BPI * this.partisanshipWeight;

        //Not else if because PA-17 has both a democrat and republican incumbent.
        if (district.isDemIncumbent()) {
            predictedDemMargin += this.demIncumbentWeight;
        }
        if (district.isRepIncumbent()) {
            predictedDemMargin += this.repIncumbentWeight;
        }

        //Convert from margin to %.
        return 0.5 + 0.5 * predictedDemMargin;
    }

    /**
     * Calculate the standard deviation of the fundamental percent of democratic vote for a given district.
     *
     * @param district The district to calculate the fundamental standard deviation for.
     * @return The standard deviation of the vote percent, where 0.01 is 1%.
     */
    public double calcFundamentalStDv(District district) {
        //We want dem incumbent xor rep incumbent because double-incumbent seats should count as open.
        if (district.isDemIncumbent() ^ district.isRepIncumbent()) {
            //Multiply by 0.5 to convert from margin standard deviation to vote percent standard deviation.
            return this.incumbentStDv * 0.5;
        } else {
            //Same here
            return this.openStDv * 0.5;
        }
    }


}
