public interface NationalCorrectionCalculator {

    double calcNationalCorrection(District district, double nationalShift);
    double calcNationalStdv(District district, double nationalShift);
}
