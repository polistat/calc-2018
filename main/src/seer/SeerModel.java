package seer;

import dataholder.District;

/**
 * Calculates the "fundamental" percent of Democratic vote for a district, given only background information about the
 * district like voting history and incumbency.
 */
public abstract class SeerModel {
    /**
     * Calculate the BPI of a district.
     */
    public abstract double calcBpi(District district);

    /**
     * Calculate the fundamental percent of the vote the Democratic candidate will get for a given district.
     *
     * @param district The district to calculate the SEER vote percent for.
     * @return The fundamental Democratic vote percent, from 0 to 1.
     */
    public abstract double calcSeerDemPercent(District district);

    /**
     * Calculate the standard deviation of the fundamental percent of Democratic vote for a given district.
     *
     * @param district The district to calculate the SEER standard deviation for.
     * @return The standard deviation of the vote percent, where 0.01 is 1%.
     */
    public abstract double calcSeerStDv(District district);

    /**
     * Calculate the fundamental percent of the vote the Democratic candidate will get, and the standard deviation, for
     * each district. This modifies each district's seerDemPercent and seerStDv.
     *
     * @param districts The districts to calculate the fundamentals for.
     */
    public void calcAll(District[] districts) {
        for (District district : districts) {
            district.setBpi(calcBpi(district));
            district.setSeerDemPercent(calcSeerDemPercent(district));
            district.setSeerStDv(calcSeerStDv(district));
        }
    }
}
