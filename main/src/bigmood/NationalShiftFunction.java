package bigmood;

/**
 * A function of the generic Democrat percent and standard deviation that gives the national shift and standard
 * deviation.
 */
public interface NationalShiftFunction {
    /**
     * @param genericDemPercent The Democratic share of the two-party vote on the national generic ballot, from 0 to 1.
     * @return The national shift to apply to each district, where 0.01 is 1% in favor of the Democrats and -0.01 is 1%
     * for the Republicans.
     */
    double getNationalShift(double genericDemPercent);

    /**
     * @param genericDemPercentStDv The standard deviation Democratic share of the two-party vote on the national
     *                              generic ballot, from 0 to 1.
     * @return The standard deviation of the national shift to apply to each district, where 0.01 is 1%.
     */
    double getNationalShiftStDv(double genericDemPercentStDv);
}
