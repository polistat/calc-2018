public class SimpleNationalCorrection extends NationalCorrectionCalculator{

    @Override
    public double calcNationalDemPercent(District district, double nationalShift) {
        double change = district.getElasticity() * nationalShift;
        district.setGenericCorrectedDemPercent(district.getFundamentalDemPercent() + change);
        return district.getGenericCorrectedDemPercent();
    }

    @Override
    public double calcNationalStDv(District district, double nationalShift) {
        district.setGenericCorrectedStDv(district.getFundamentalStDv());
        return district.getGenericCorrectedStDv();
    }
}
