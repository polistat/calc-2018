public abstract class NationalCorrectionCalculator {

    public abstract double calcNationalDemPercent(District district, double nationalShift);

    public abstract double calcNationalStDv(District district, double nationalShift);

    public void calcAll(District[] districts, double nationalShift) {
        for (District district : districts) {
            if (district.isContested()) {
                calcNationalDemPercent(district, nationalShift);
                calcNationalStDv(district, nationalShift);
            }
        }
    }
}
