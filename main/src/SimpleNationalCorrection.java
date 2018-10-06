public class SimpleNationalCorrection extends NationalCorrectionCalculator {

    @Override
    public double calcNationalDemPercent(District district, double nationalShift) {
        return district.getFundamentalDemPercent() + district.getElasticity() * nationalShift;
    }

    @Override
    public double calcNationalStDv(District district, double nationalShift) {
        return district.getFundamentalStDv();
    }
}
