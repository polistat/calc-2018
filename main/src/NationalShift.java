public class NationalShift extends NationalCorrectionCalculator{

    public NationalShift(double nationalShift) {
        super(nationalShift);
    }

    @Override
    public double calcNationalCorrection(District district) {
        double change = district.getElasticity() * nationalShift;
        district.setNationalCorrectionMargin(district.getFundamentalMargin() + change);
        return district.getNationalCorrectionMargin();
    }

    @Override
    public double calcNationalStdv(District district) {
        district.setNationalCorrectionStdv(district.getFundamentalStdv());
        return district.getNationalCorrectionStdv();
    }
}
