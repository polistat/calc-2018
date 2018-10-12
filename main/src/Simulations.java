import auspice.PollCalculator;
import bigmood.NationalCorrectionCalculator;
import bigmood.NationalShiftCalculator;
import bigmood.NationalShiftFunction;
import dataholder.District;
import util.Normal;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Random;

/**
 * Runs the simulations to determine the actual democrat win % for the country.
 */
public class Simulations {

    /**
     * Simulate the house races and write the histogram of how many seats Democrats win to one file, and the
     * per-district stats to another.
     *
     * @param districts                    The districts to simulate, with fundamentals already calculated.
     * @param genericAverage               The average dem percent in the national generic ballot.
     * @param genericStDv                  The standard deviation of the national generic ballot.
     * @param nationalShiftCalculator      The object to calculate the national shift from the generic ballot.
     * @param nationalCorrectionCalculator The correction calculator to adjust each district according to the national
     *                                     shift.
     * @param pollCalculator               The poll calculator to adjust each district according to that district's poll
     *                                     or Blairvoyance.
     * @param iterations                   The number of generic ballots to simulate.
     * @return The probability that Democrats win a majority in the House.
     * @throws IOException If the file writing fails.
     */
    public static double write(District[] districts, double genericAverage, double genericStDv,
                               NationalShiftCalculator nationalShiftCalculator,
                               NationalCorrectionCalculator nationalCorrectionCalculator, PollCalculator pollCalculator,
                               int iterations) throws IOException {
        Random generator = new Random();

        //Outputs info about each district.
        PrintWriter out1 = new PrintWriter(new BufferedWriter(new FileWriter("district_results.csv")));
        //Outputs the histogram of Democrat seats won.
        PrintWriter out2 = new PrintWriter(new BufferedWriter(new FileWriter("histogram.csv")));

        //Record how many seats Democrats won in each simulation.
        double[] histogram = new double[districts.length + 1];

        //Record the average chance the democrats win each district.
        double[] avgDistrictWinChances = new double[districts.length];

        // Natl shift calculator
        NationalShiftFunction calc = nationalShiftCalculator.getFunction(districts);
        System.out.println("Shift standard deviation: " + calc.getNationalShiftStDv(genericStDv));

        double avgExpectedSeats = 0;

        //Simulate different generic ballots
        for (int i = 0; i < iterations; i++) {
            //Calculate expected dem vote percentage and standard deviation, given a generic ballot percent.
            double nationalShift = calc.getNationalShift(genericAverage);
            nationalCorrectionCalculator.calcAll(districts, nationalShift);
            pollCalculator.calcAll(districts);
            double noise = calc.getNationalShiftStDv(genericStDv) * generator.nextGaussian();

            //Check each district
            int expectedSeats = 0;
            for (int j = 0; j < districts.length; j++) {
                double winChance;
                //util.Normal.normalCDF doesn't like standard deviations of 0, so we handle that here.
                if (districts[j].getAuspiceStDv() == 0) {
                    winChance = districts[j].getAuspiceDemPercent() > 0.5 ? 1 : 0;
                } else {
                    //Since the vote percent is normally distributed, we can just calculate the chance that democrats
                    // win.
                    winChance = 1 - Normal.normalCDF(districts[j].getAuspiceDemPercent() + noise * districts[j].getElasticity(),
                            Math.sqrt(Math.pow(districts[j].getAuspiceStDv(), 2) - Math.pow(genericStDv * districts[j].getElasticity(), 2)),
                            0.5);
                    //If a win chance is less than 0% or more than 100%, something has gone horribly wrong.
                    if (winChance > 1 || winChance < 0) {
                        System.out.println("aaaaaaaaaaaa " + districts[j].getName());
                    }
                }
                if (Math.random() < winChance) {
                    //The expected number of seats is the sum of the percent chance that democrats win each seat.
                    expectedSeats++;
                    //Sum now, divide later.
                    avgDistrictWinChances[j]++;
                }
            }
            //Histogram uses integers, so we round.
            histogram[expectedSeats] += 1;
            avgExpectedSeats += expectedSeats;
        }

        //Divide the expected seats
        avgExpectedSeats /= iterations;

        //Divide the district win chances.
        for (int i = 0; i < avgDistrictWinChances.length; i++) {
            avgDistrictWinChances[i] /= iterations;
        }

        LocalDate today = LocalDate.now();

        //Recalculate dem % using average shift
        double nationalShift = calc.getNationalShift(genericAverage);
        nationalCorrectionCalculator.calcAll(districts, nationalShift);
        pollCalculator.calcAll(districts);

        //Record the day this simulation was run, the final expected democratic vote percent, the standard deviation
        // of the vote percent, the chance democrats win the district, the SEER percent, and the bigmood percent.
        for (int i = 0; i < districts.length; i++) {
            out1.println(today.getYear() + "," + today.getMonthValue() + "," +
                    today.getDayOfMonth() + "," + districts[i].getName() + ","
                    + districts[i].getAuspiceDemPercent() + "," + districts[i].getAuspiceStDv() + ","
                    + avgDistrictWinChances[i] + "," + districts[i].getSeerDemPercent() + ","
                    + districts[i].getBigmoodDemPercent());
        }


        //Record the normalized histogram.
        for (double timesDemWonN : histogram) {
            out2.println((timesDemWonN / iterations));
        }

        //Count how many times the democrats won control of the House.
        double totalDemProb = 0;
        for (int i = 218; i < histogram.length; i++) {
            totalDemProb += histogram[i];
        }

        //Make it a probability
        out2.println(totalDemProb / iterations);

        //Add expected seats
        out2.println(avgExpectedSeats);

        out1.close();
        out2.close();
        return totalDemProb / iterations;
    }

}
