import java.time.LocalDate;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
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

		double[] probabilities = calculateProbabilities(districts, genericAverage, genericStDv, pollCalculator,
				nationalCorrectionCalculator, nationalShiftCalculator, iterations);
		double[] histogram = makeHistogram(probabilities, iterations);
		
		LocalDate today = LocalDate.now();
		
		for (int i = 0; i < districts.length; i++) {
			out1.println(today.getYear() + "," + today.getMonth() + "," + 
					today.getDayOfMonth() + "," + districts[i].getName() + ","
					+ districts[i].getFinalDemPercent() + "," + districts[i].getFinalStDv() + "," + probabilities[i]);
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
	
	public static double[] calculateProbabilities(District[] districts, double genericAverage, double genericStDv,
												  PollCalculator pollCalculator, NationalCorrectionCalculator nationalCorrectionCalculator,
												  NationalShiftCalculator nationalShiftCalculator, int iterations) throws IOException {
		double[] demWinChances = new double[districts.length];
		Random generator = new Random();

		for (int i = 0; i < iterations; i++) {
			double genericBallot = genericAverage + generator.nextGaussian()*genericStDv;
			System.out.println(genericStDv);
			double nationalShift = nationalShiftCalculator.calcNationalShift(districts, genericBallot);
//			System.out.println("Shift: "+nationalShift);
			nationalCorrectionCalculator.calcAll(districts, nationalShift);
			pollCalculator.calcAll(districts);
			for (int j = 0; j < districts.length; j++) {
				double predictedDemPercent = generator.nextGaussian() * districts[j].getFinalStDv() + districts[j].getFinalDemPercent();
				if (predictedDemPercent > 0.5) {
					demWinChances[j]++;
				}

//				if (j == 1){
//					System.out.println(districts[j].getFinalDemPercent());
//					System.out.println(districts[j].getFinalStDv());
//					System.out.println(predictedDemPercent);
//				}
			}
		}

		for (int i = 0; i < demWinChances.length; i++){
			demWinChances[i] = demWinChances[i]/iterations;
		}

		System.out.println(Arrays.toString(demWinChances));

		return demWinChances;
	}
	
	public static double[] makeHistogram(double[] demWinChances, int iterations) {
		double[] distribution = new double[demWinChances.length + 1];
		for (int i = 0; i < iterations; i++) {
			int demWins = 0;
			for (double aRaceProb : demWinChances) {
				if (binGen(aRaceProb)) {
					demWins++;
				}
			}
			distribution[demWins]++;
		}
		return distribution;
	}
	
	public static boolean binGen(double prob) {
		Random generator = new Random();
		return (generator.nextDouble() < prob);
	}

}
