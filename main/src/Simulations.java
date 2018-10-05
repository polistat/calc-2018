import java.time.LocalDate;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;


public class Simulations {


	public static void main(String[] args) throws IOException {
		
	}
	
	public static double write(District[] districts, double genericAverage, double genericStDv,
							   PollCalculator pollCalculator, NationalCorrectionCalculator nationalCorrectionCalculator,
							   NationalShiftCalculator nationalShiftCalculator, int iterations) throws IOException {

	    PrintWriter out1 = new PrintWriter(new BufferedWriter(new FileWriter("district_results.csv")));
		PrintWriter out2 = new PrintWriter(new BufferedWriter(new FileWriter("histogram.csv")));
		// output: margin with stdv, probability of winning, histogram

        double[] histogram = new double[districts.length + 1];
        Random generator = new Random();

        double[] demWinChance = new double[districts.length];
        double[] avgDistrictWinChances = new double[districts.length];
        for (int i = 0; i < iterations; i++) {
			double genericBallot = genericAverage + generator.nextGaussian()*genericStDv;
			double nationalShift = nationalShiftCalculator.calcNationalShift(districts, genericBallot);
            nationalCorrectionCalculator.calcAll(districts, nationalShift);
            pollCalculator.calcAll(districts);
            double expectedSeats = 0;
            for (int j = 0; j < districts.length; j++) {
                demWinChance[j] = 1 - Normal.normalCDF(districts[j].getFinalDemPercent(), districts[j].getFinalStDv(), 0.5);
                expectedSeats += demWinChance[j];
                avgDistrictWinChances[j] += demWinChance[j];
            }
            histogram[(int) Math.round(expectedSeats)] += 1;
        }

        for (int i = 0; i < avgDistrictWinChances.length; i++){
            avgDistrictWinChances[i] /= iterations;
        }
		
		LocalDate today = LocalDate.now();
		
		for (int i = 0; i < districts.length; i++) {
			out1.println(today.getYear() + "," + today.getMonth() + "," +
					today.getDayOfMonth() + "," + districts[i].getName() + ","
					+ districts[i].getFinalDemPercent() + "," + districts[i].getFinalStDv() + "," + avgDistrictWinChances[i]);
		}


		for (double timesDemWonN : histogram) {
			out2.println((timesDemWonN / iterations));
		}
		
		double totalDemProb = 0;
		for (int i = 218; i < histogram.length; i++) {
			totalDemProb += histogram[i];
		}

		out2.println(totalDemProb/iterations);
			
		
		out1.close();
		out2.close();
		return totalDemProb/iterations;
	}

}
