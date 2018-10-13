package bigmood;

/**
 * A simple function of the generic Democrat percent and standard deviation that gives the national shift and standard
 * deviation.
 */
public class SimpleNationalShiftFunction implements NationalShiftFunction {

    /**
     * What percent of the national vote we expect Democrats to receive based only on fundamentals.
     */
    private final double demBaseLine;
    /**
     * The standard deviation of the percent of the national vote we expect Democrats to receive based only on
     * fundamentals.
     */
    private final double demBaseLineStDv;

    /**
     * Default constructor.
     *
     * @param demBaseLine     What percent of the national vote we expect Democrats to receive based only on
     *                        fundamentals.
     * @param demBaseLineStDv The standard deviation of the percent of the national vote we expect Democrats to receive
     *                        based only on fundamentals.
     */
    public SimpleNationalShiftFunction(double demBaseLine, double demBaseLineStDv) {
        this.demBaseLine = demBaseLine;
        this.demBaseLineStDv = demBaseLineStDv;
    }

    /**
     * @param genericDemPercent The Democratic share of the two-party vote on the national generic ballot, from 0 to 1.
     * @return The national shift to apply to each district, where 0.01 is 1% in favor of the Democrats and -0.01 is 1%
     * for the Republicans.
     */
    @Override
    public double getNationalShift(double genericDemPercent) {
        return genericDemPercent - demBaseLine;
    }

    /**
     * @param genericDemPercentStDv The standard deviation Democratic share of the two-party vote on the national
     *                              generic ballot, from 0 to 1.
     * @return The standard deviation of the national shift to apply to each district, where 0.01 is 1%.
     */
    @Override
    public double getNationalShiftStDv(double genericDemPercentStDv) {
        return Math.sqrt(Math.pow(genericDemPercentStDv, 2) + Math.pow(demBaseLineStDv, 2));
    }
}