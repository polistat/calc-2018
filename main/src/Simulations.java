import dataholder.District;
import util.Normal;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Random;

/**
 * Runs the simulations to determine the actual Democrat win % for the country.
 */
public class Simulations {

    /**
     * Simulate the house races and write the histogram of how many seats Democrats win to one file, and the
     * per-district stats to another.
     *
     * @param districts         The districts to simulate, with fundamentals already calculated.
     * @param nationalShiftStDv The standard deviation of the national shift, to be applied as noise to the AUSPICE
     *                          predictions.
     * @param iterations        The number of generic ballots to simulate.
     * @return The probability that Democrats win a majority in the House.
     * @throws IOException If the file writing fails.
     */
    public static double write(District[] districts, double nationalShiftStDv, int iterations) throws IOException {
        Random generator = new Random();

        //Outputs info about each district.
        PrintWriter out1 = new PrintWriter(new BufferedWriter(new FileWriter("district_results.csv")));
        //Outputs the histogram of Democrat seats won.
        PrintWriter out2 = new PrintWriter(new BufferedWriter(new FileWriter("histogram.csv")));

        //Record how many seats Democrats won in each simulation.
        double[] histogram = new double[districts.length + 1];

        //Record the average chance the Democrats win each district.
        double[] avgDistrictWinChances = new double[districts.length];

        //How many seats the Democrats will win, on average.
        double avgExpectedSeats = 0;

        //Simulate different generic ballots
        for (int i = 0; i < iterations; i++) {
            //Calculate shift error for this run.
            double noise = nationalShiftStDv * generator.nextGaussian();

            //Check each district
            int expectedSeats = 0;
            for (int j = 0; j < districts.length; j++) {
                double winChance;
                //Normal.normalCDF doesn't like standard deviations of 0, so we handle that here.
                if (districts[j].getAuspiceStDv() == 0) {
                    winChance = districts[j].getAuspiceDemPercent() > 0.5 ? 1 : 0;
                } else {
                    //Since the vote percent is normally distributed, we can just calculate the chance that Democrats
                    // win.
                    winChance =
                            1 - Normal.normalCDF(districts[j].getAuspiceDemPercent() + noise * districts[j].getElasticity(),
                                    Math.sqrt(Math.pow(districts[j].getAuspiceStDv(), 2) - Math.pow(nationalShiftStDv * districts[j].getElasticity(), 2)),
                                    0.5);
                    //If a win chance is less than 0% or more than 100%, something has gone horribly wrong.
                    if (winChance > 1 || winChance < 0) {
                        System.out.println("aaaaaaaaaaaa " + districts[j].getName());
                    }
                }
                if (Math.random() < winChance) {
                    //The expected number of seats is the sum of the percent chance that Democrats win each seat.
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

        //Record the day this simulation was run, the AUSPICE Democratic vote percent, the standard deviation of the
        // AUSPICE percent, the chance Democrats win the district, the SEER percent, and the bigmood percent.
        out1.println("year,month,day,district,auspice,stdev,chance,seer,bigmood,bpi");
        for (int i = 0; i < districts.length; i++) {
            out1.println(today.getYear() + "," + today.getMonthValue() + "," +
                    today.getDayOfMonth() + "," + districts[i].getName() + ","
                    + districts[i].getAuspiceDemPercent() + "," + districts[i].getAuspiceStDv() + ","
                    + avgDistrictWinChances[i] + "," + districts[i].getSeerDemPercent() + ","
                    + districts[i].getBigmoodDemPercent() + "," + districts[i].getBpi());
        }


        //Record the normalized histogram.
        for (double timesDemWonN : histogram) {
            out2.println((timesDemWonN / iterations));
        }

        //Count how many times the Democrats won control of the House.
        double totalDemProb = 0;
        for (int i = 218; i < histogram.length; i++) {
            totalDemProb += histogram[i];
        }

        //Make it a probability
        out2.println(totalDemProb / iterations);

        //Add expected seats
        out2.println(avgExpectedSeats);
        System.out.println("Average expected seats: " + avgExpectedSeats);

        out1.close();
        out2.close();
        
        
        //aggregates and creates state_results output
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("state_results.csv")));
        String currState = "AL";
        int distCount = 0;
        double currMean = 0;
        double currStDev = 0;
        pw.println("State, Mean, StDev");
        for(District d : districts){
        	pw.flush();
        	if(!d.getName().substring(0,2).contentEquals(currState)){
        		pw.println(currState + "," + (currMean / distCount) + "," + (Math.sqrt(currStDev) / distCount));
        		currState = d.getName().substring(0,2);
        		distCount = 0;
        		currMean = 0;
        		currStDev = 0;
        	}
        	distCount++;
        	currMean += d.getAuspiceDemPercent();
        	currStDev += d.getAuspiceStDv()*d.getAuspiceStDv();
        	if(!d.isContested()){
        		currMean -= d.getAuspiceDemPercent();
            	currStDev -= d.getAuspiceStDv()*d.getAuspiceStDv();
        		currMean += d.getSeerDemPercent();
            	currStDev += d.getSeerStDv()*d.getSeerStDv();
        	}
        }
        pw.println(currState + "," + (currMean / distCount) + "," + (Math.sqrt(currStDev) / distCount));
        pw.close();
        
        
        return totalDemProb / iterations;
    }

}
