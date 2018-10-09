
public interface NationalShiftFunction {
	/**
	 * @param genericDemPercent The democratic share of the two-party vote on the
	 *                          national generic ballot, from 0 to 1.
	 * @return The national shift to apply to each district, where 0.01 is 1% in
	 *         favor of the democrats and -0.01 is 1% for the republicans.
	 * 
	 */
	double getNationalShift(double genericDemPercent);
}
