import auspice.*;
import bigmood.*;
import dataholder.District;
import dataholder.Grade;
import dataholder.Poll;
import seer.LinearSeerModel;
import seer.SeerModel;
import util.DataReader;

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

        double[] avgGradeStDvs = {0.056503, 0.066247, 0.076677, 0.1303};

        //Which states got redistricted when is hard-coded.
        Set<String> redistricted2018 = new HashSet<>();
        redistricted2018.add("PA");
        Set<String> redistricted2016 = new HashSet<>();
        redistricted2016.add("NC");

        //Simple linear fundamentals.
        SeerModel seerModel = new LinearSeerModel(0.133,
                0.278, 0.244, 0.345, 2,
                0.06815999728, -0.08171914802, 0.132, 0.149);

        //Use Daniel's thing because it's better.
        NationalShiftCalculator natlShiftCalc = new DZhuNatlShiftCalc("2014.csv",
                "2016.csv", redistricted2018, redistricted2016);

        //Read in the districts
        District[] districts = DataReader.parseFromCSV("district_input.csv", "poll_input.csv",
                "bv_out.csv");

        //Shift average poll standard deviations to match historical data.
        PollStDvShifter.shiftPolls(districts, avgGradeStDvs);

        //Calculate fundamentals
        seerModel.calcAll(districts);

        //Average the generic ballot polls using an exponential averager.
        PollAverager nationalPollAverager = new ExponentialPollAverager(1. / 30.);

        //Read in the generic ballot polls
        Poll[] nationalPolls = DataReader.readNationalPolls("national_polls.csv");

        //Find the average of the generic ballot polls
        double nationalPollAverage = nationalPollAverager.getAverage(nationalPolls);

        // National shift calculator
        NationalShiftFunction nationalShiftFunction = natlShiftCalc.getFunction(districts);

        //National shift
        double nationalShift = nationalShiftFunction.getNationalShift(nationalPollAverage);

        //National shift standard deviation
        double nationalShiftStDv = nationalShiftFunction.getNationalShiftStDv(0.0138);

        //Log generic ballot average and the corresponding shift.
        System.out.println("National average: " + Math.round(nationalPollAverage * 10000.) / 100. + "%");
        System.out.println("Mean shift: " + Math.round(nationalShift * 10000.) / 100. + " percentage points");
        System.out.println("Shift standard deviation: " + Math.round(nationalShiftStDv * 10000.) / 100. + " " +
                "percentage points");

        //Define the bigmood model.
        BigmoodModel bigmoodModel = new SimpleBigmoodModel();

        //Calculate bigmood
        bigmoodModel.calcAll(districts, nationalShift);

        //Define the poll averager for district-level polls.
        PollAverager pollAverager = new ExponentialPollAverager(1. / 30.);
        BlairvoyanceWeightCalculator bwCalc = district -> Math.abs(2*district.getBpi()-1);

        //Weight the polls vs SEER using arctan.
        AuspiceModel auspiceModel = new ArctanAuspiceModel(pollAverager, bwCalc, gradeQualityPoints, 1. / 167.,
                0.95, 0, 6.12, 0.265, 0.086500555313);

        //Calculate AUSPICE
        auspiceModel.calcAll(districts);

        //Run simulations
        System.out.println("Dem win chance: " + Math.round(10000. * Simulations.write(districts, nationalShiftStDv,
                10000000)) / 100. + "%");
    }
}
