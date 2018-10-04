public abstract class NationalCorrectionCalculator {

    protected final double nationalShift;

    public NationalCorrectionCalculator(double nationalShift){
        this.nationalShift = nationalShift;
    }

    public abstract double calcNationalDemPercent(District district);
    public abstract double calcNationalStDv(District district);

    public void calcAll(District[] districts){
        for (District district : districts){
            calcNationalDemPercent(district);
            calcNationalStDv(district);
        }
    }
}
