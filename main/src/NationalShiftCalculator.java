import java.io.IOException;

public interface NationalShiftCalculator {

    double calcNationalShift(District[] districts, double genericDemPercent) throws IOException;
}
