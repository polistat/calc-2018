package bigmood;

import dataholder.District;

/**
 * Applies the national correction by multiplying by elasticity then adding to fundamental percent.
 */
public class SimpleNationalCorrection extends NationalCorrectionCalculator {

    /**
     * Calculate the percent of the vote the democratic candidate will get for a given district, adjusted for national
     * mood.
     *
     * @param district      The district to calculate. Must already have a fundamental dem percent calculated.
     * @param nationalShift The number of percentage points to shift the average district by, according to national
     *                      mood. 1% is 0.01.
     * @return The updated percent of the vote the democratic candidate will get, from 0 to 1.
     */
    @Override
    public double calcNationalDemPercent(District district, double nationalShift) {
        return district.getFundamentalDemPercent() + district.getElasticity() * nationalShift;
    }

    /**
     * Calculate the standard deviation percent of the vote the democratic candidate will get for a given district,
     * adjusted for national mood.
     *
     * @param district      The district to calculate. Must already have a fundamental dem standard deviation
     *                      calculated.
     * @param nationalShift The number of percentage points to shift the average district by, according to national
     *                      mood. 1% is 0.01.
     * @return The updated standard deviation of the percent of the vote the democratic candidate will get, where 1
     * percentage point is 0.01.
     */
    @Override
    public double calcNationalStDv(District district, double nationalShift) {
        return district.getFundamentalStDv();
    }
}
