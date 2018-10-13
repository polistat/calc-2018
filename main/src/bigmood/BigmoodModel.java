package bigmood;

import dataholder.District;

/**
 * Use the national shift to adjust an individual district's expected Democratic vote percent.
 */
public abstract class BigmoodModel {

    /**
     * Calculate the percent of the vote the Democratic candidate will get for a given district, adjusted for national
     * mood.
     *
     * @param district      The district to calculate. Must already have a SEER dem percent calculated.
     * @param nationalShift The number of percentage points to shift the average district by, according to national
     *                      mood. 1% is 0.01.
     * @return The updated percent of the vote the Democratic candidate will get, from 0 to 1.
     */
    public abstract double calcBigmoodDemPercent(District district, double nationalShift);

    /**
     * Calculate the standard deviation percent of the vote the Democratic candidate will get for a given district,
     * adjusted for national mood.
     *
     * @param district      The district to calculate. Must already have a SEER dem standard deviation calculated.
     * @param nationalShift The number of percentage points to shift the average district by, according to national
     *                      mood. 1% is 0.01.
     * @return The updated standard deviation of the percent of the vote the Democratic candidate will get, where 1
     * percentage point is 0.01.
     */
    public abstract double calcBigmoodStDv(District district, double nationalShift);

    /**
     * Calculate the percent of the vote the Democratic candidate will get, and the standard deviation, for each
     * competitive district. This modifies each competitive district's bigmoodDemPercent and bigmoodStDv.
     *
     * @param districts     The districts to calculate the mood-adjusted dem percent for.
     * @param nationalShift The number of percentage points to shift the average district by, according to national
     *                      mood. 1% is 0.01.
     */
    public void calcAll(District[] districts, double nationalShift) {
        for (District district : districts) {
            if (district.isContested()) {
                district.setBigmoodDemPercent(calcBigmoodDemPercent(district, nationalShift));
                district.setBigmoodStDv(calcBigmoodStDv(district, nationalShift));
            }
        }
    }
}
