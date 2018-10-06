/**
 * Calculates the "fundamental" percent of democratic vote for a district, given only background information about the
 * district like voting history and incumbency.
 */
public abstract class FundamentalCalculator {

    /**
     * Calculate the fundamental percent of the vote the democratic candidate will get for a given district.
     *
     * @param district The district to calculate the fundamental vote percent for.
     * @return The fundamental democratic vote percent, from 0 to 1.
     */
    public abstract double calcFundamentalDemPercent(District district);

    /**
     * Calculate the standard deviation of the fundamental percent of democratic vote for a given district.
     *
     * @param district The district to calculate the fundamental standard deviation for.
     * @return The standard deviation of the vote percent, where 0.01 is 1%.
     */
    public abstract double calcFundamentalStDv(District district);

    /**
     * Calculate the fundamental percent of the vote the democratic candidate will get, and the standard deviation, for
     * each district. This modifies each district's fundamentalDemPercent and fundamentalStDv.
     *
     * @param districts The districts to calculate the fundamentals for.
     */
    public void calcAll(District[] districts) {
        for (District district : districts) {
            district.setFundamentalDemPercent(calcFundamentalDemPercent(district));
            district.setFundamentalStDv(calcFundamentalStDv(district));
        }
    }
}
