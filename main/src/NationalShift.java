public class NationalShift implements  NationalCorrectionCalculator{

    @Override
    public double calcNationalCorrection(District district, double nationalShift) {
        double change = district.getElasticity() * nationalShift;
        district.setNationalCorrectionMargin(district.getFundamentalMargin() + change);
        return district.getNationalCorrectionMargin();
    }

    @Override
    public double calcNationalStdv(District district, double nationalShift) {
        district.setNationalCorrectionStdv(district.getFundamentalStdv());
        return district.getNationalCorrectionStdv();
    }
}
