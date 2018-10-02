public abstract class NationalCorrectionCalculator {

    protected final double nationalShift;

    public NationalCorrectionCalculator(double nationalShift){
        this.nationalShift = nationalShift;
    }

    public abstract double calcNationalCorrection(District district);
    public abstract double calcNationalStdv(District district);
}
