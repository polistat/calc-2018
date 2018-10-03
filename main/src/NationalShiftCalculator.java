import java.io.IOException;

public interface NationalShiftCalculator {

    double calcNationalShift(String lastCongressionalDataFile, District[] districts, double genericDemPercent) throws IOException;
}
