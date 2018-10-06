/**
 * Calculates the national shift from the democratic percent of the vote on the national generic ballot.
 */
public interface NationalShiftCalculator {

    /**
     * Calculate the national shift.
     *
     * @param districts         A list of every district, with the fundamental dem win percent filled in.
     * @param genericDemPercent The democratic share of the two-party vote on the national generic ballot, from 0 to 1.
     * @return The national shift to apply to each district, where 0.01 is 1% in favor of the democrats and -0.01 is 1%
     * for the republicans.
     */
    double calcNationalShift(District[] districts, double genericDemPercent);
}
