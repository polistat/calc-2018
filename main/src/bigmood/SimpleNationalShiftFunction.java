package bigmood;

public class SimpleNationalShiftFunction implements NationalShiftFunction{

    private final double demBaseLine;
    private final double demBaseLineStDv;

    public SimpleNationalShiftFunction(double demBaseLine, double demBaseLineStDv) {
        this.demBaseLine = demBaseLine;
        this.demBaseLineStDv = demBaseLineStDv;
    }

    /**
     * @param genericDemPercent The democratic share of the two-party vote on the national generic ballot, from 0 to 1.
     * @return The national shift to apply to each district, where 0.01 is 1% in favor of the democrats and -0.01 is 1%
     * for the republicans.
     */
    @Override
    public double getNationalShift(double genericDemPercent) {
        return genericDemPercent - demBaseLine;
    }

    /**
     * @param genericDemPercentStDv The standard deviation democratic share of the two-party vote on the national
     *                              generic ballot, from 0 to 1.
     * @return The standard deviation of the national shift to apply to each district, where 0.01 is 1%.
     */
    @Override
    public double getNationalShiftStDv(double genericDemPercentStDv) {
        return Math.sqrt(Math.pow(genericDemPercentStDv, 2) + Math.pow(demBaseLineStDv, 2));
    }
}