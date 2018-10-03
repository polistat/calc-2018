public class NationalShift extends NationalCorrectionCalculator{

    public NationalShift(double nationalShift) {
        super(nationalShift);
    }

    @Override
    public double calcNationalDemPercent(District district) {
        double change = district.getElasticity() * nationalShift;
        district.setGenericCorrectedDemPercent(district.getFundamentalDemPercent() + change);
        return district.getGenericCorrectedDemPercent();
    }

    @Override
    public double calcNationalStDv(District district) {
        district.setGenericCorrectedStDv(district.getFundamentalStDv());
        return district.getGenericCorrectedStDv();
    }
}
