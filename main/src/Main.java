import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Central class for running everything.
 */
public class Main {

    /**
     * Main method that gets run.
     *
     * @param args Not used.
     * @throws IOException If a file needed somewhere is improperly formatted or missing.
     */
    public static void main(String[] args) throws IOException {
        //Grade quality points are hard-coded.
        Map<Grade, Double> gradeQualityPoints = new HashMap<>();
        gradeQualityPoints.put(Grade.A, 0.176981753181247);
        gradeQualityPoints.put(Grade.B, 0.150950231708606);
        gradeQualityPoints.put(Grade.C, 0.130417204637636);
        gradeQualityPoints.put(Grade.D, 0.076745970836531);

        //Which states got redistricted when is hard-coded.
        Set<String> redistricted2018 = new HashSet<>();
        redistricted2018.add("PA");
        Set<String> redistricted2016 = new HashSet<>();
        redistricted2016.add("NC");

        //Simple linear fundamentals.
        FundamentalCalculator fundamentalCalculator = new LinearFundamentalCalculator(0.133,
                0.278, 0.244, 0.345, 1.1645,
                0.1558, -0.1405, 0.1, 0.15);

        //Use Daniel's thing because it's better.
        NationalShiftCalculator natlShiftCalc = new DZhuNatlShiftCalc("2014.csv",
                "2016.csv", redistricted2018, redistricted2016);

        //Read in the districts
        District[] districts = District.parseFromCSV("district_input.csv", "poll_input.csv",
                "blairvoyance_input.csv");

        //Calculate fundamentals
        fundamentalCalculator.calcAll(districts);

        //Average the generic ballot polls using an exponential averager.
        PollAverager nationalPollAverager = new ExponentialPollAverager(1. / 30.);

        //Read in the generic ballot polls
        Poll[] nationalPolls = Poll.readNationalPolls("national_polls.csv");

        //Find the average of the generic ballot polls
        double nationalPollAverage = nationalPollAverager.getAverage(nationalPolls);

        //Log generic ballot average and the corresponding shift.
        System.out.println("National average: " + Math.round(nationalPollAverage * 10000.) / 100. + "%");
        System.out.println("Mean shift: " + Math.round(natlShiftCalc.calcNationalShift(districts,
                nationalPollAverage) * 10000.) / 100. + " percentage points");

        //Define the national correction calculator.
        NationalCorrectionCalculator natlCorrectCalc = new SimpleNationalCorrection();

        //Define the poll averager for district-level polls.
        PollAverager pollAverager = new ExponentialPollAverager(1. / 30.);

        //Weight the polls vs fundamentals using arctan.
        PollCalculator pollCalculator = new ArctanPollCalculator(pollAverager, gradeQualityPoints, 1. / 167.,
                0.9, 0, 16.6, 0.0, 0.05);

        //Run simulations
        System.out.println("Dem win chance: " + (100. * Simulations.write(districts, nationalPollAverage, 0.02,
                natlShiftCalc, natlCorrectCalc, pollCalculator,
                100000)) + "%");
    }
}
