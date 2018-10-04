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
							   int iterations) throws IOException {
		PrintWriter out1 = new PrintWriter(new BufferedWriter(new FileWriter("district_results.csv")));
		PrintWriter out2 = new PrintWriter(new BufferedWriter(new FileWriter("histogram.csv")));
		// output: margin with stdv, probability of winning, histogram
		
		double[] racePerc = new double[districts.length];
		double[] raceStdv = new double[districts.length];
		for (int i = 0; i < districts.length; i++) {
			racePerc[i] = districts[i].getFinalDemPercent();
			raceStdv[i] = districts[i].getFinalStDv();
		}

		double[] probs = percToProb(districts, genericAverage, genericStDv, pollCalculator,
				nationalCorrectionCalculator, iterations);
		double[] histo = probSimulate(probs, iterations);
		
		LocalDate today = LocalDate.now();
		
		for (int i = 0; i < districts.length; i++) {
			out1.println(today.getYear() + "," + today.getMonth() + "," + 
					today.getDayOfMonth() + "," + districts[i].getName() + ","
					+ racePerc[i] + "," + raceStdv[i] + "," + probs[i]);
		}
		for (double aHisto : histo) {
			out2.println((aHisto / iterations));
		}
		
		double totalDemProb = 0;
		for (int i = 0; i < histo.length; i++) {
			if (i >= 218) totalDemProb += histo[i];
		}
		out2.println(totalDemProb/iterations);
			
		
		out1.close();
		out2.close();
		return totalDemProb/iterations;
	}
	
	public static double[] percToProb(District[] districts, double genericAverage, double genericStDv,
									  PollCalculator pollCalculator, NationalCorrectionCalculator nationalCorrectionCalculator,
									  int iterations) {
		double[] raceProb = new double[districts.length];
		Random generator = new Random();

		for (int i = 0; i < iterations; i++) {
			double genericBallot = genericAverage + generator.nextGaussian()*genericStDv;
			System.out.println("Generic ballot: "+genericBallot);
			nationalCorrectionCalculator.calcAll(districts, genericBallot);
			pollCalculator.calcAll(districts);
			for (int j = 0; j < districts.length; j++) {
				if (generator.nextGaussian() * districts[j].getFinalStDv() + districts[j].getFinalDemPercent() > 0.5) {
					raceProb[j]++;
				}
			}
		}

		for (int i = 0; i < raceProb.length; i++){
			raceProb[i] = raceProb[i]/iterations;
		}

		return raceProb;
	}
	
	public static double[] probSimulate(double[] raceProb, int iter) {
		double[] distribution = new double[raceProb.length + 1];
		for (int i = 0; i < iter; i++) {
			int demWins = 0;
			for (double aRaceProb : raceProb) {
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
