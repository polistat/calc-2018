import java.time.LocalDate;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;


public class Simulations {


	public static void main(String[] args) throws IOException {
		
	}
	
	public static double write(District[] districts, int iterations) throws IOException {
		PrintWriter out1 = new PrintWriter(new BufferedWriter(new FileWriter("districtMarg.csv")));
		PrintWriter out2 = new PrintWriter(new BufferedWriter(new FileWriter("histogram.csv")));
		// output: margin with stdv, probability of winning, histogram
		
		double[] racePerc = new double[districts.length];
		double[] raceStdv = new double[districts.length];
		for (int i = 0; i < districts.length; i++) {
			racePerc[i] = districts[i].getFinalDemPercent();
			raceStdv[i] = districts[i].getFinalStDv();
		}

		double[] probs = percToProb(racePerc, raceStdv, iterations);
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
	
	
	// unnecessary
	public static double[] simulate(District[] districts) {
		double[] racePerc = new double[districts.length];
		double[] raceStdv = new double[districts.length];
		for (int i = 0; i < districts.length; i++) {
			racePerc[i] = districts[i].getFinalDemPercent();
			raceStdv[i] = districts[i].getFinalStDv();
		}
		double[] probs = percToProb(racePerc, raceStdv, 1000);
		return probSimulate(probs, 10000);
	}
	
	public static double[] percToProb(double[] racePerc, double[] raceStdv, int iter) {
		double[] raceProb = new double[racePerc.length];
		Random generator = new Random();
		for (int i = 0; i < racePerc.length; i++) {
			double demWins = 0.0;
			for (int j = 0; j < iter; j++) {
				if (generator.nextGaussian() * raceStdv[i] + racePerc[i] > 0.5) {
					demWins++;
				}
			}
			raceProb[i] = demWins / iter;
		}
		return raceProb;
	}
	
	public static double[] probSimulate(double[] raceProb, int iter) {
		double[] distri = new double[raceProb.length + 1];
		for (int i = 0; i < iter; i++) {
			int demWins = 0;
			for (int j = 0; j < raceProb.length; j++) {
				if (binGen(raceProb[j])) demWins++;
			}
			distri[demWins]++;
		}
		return distri;
	}
	
	public static boolean binGen(double prob) {
		Random generator = new Random();
		return (generator.nextDouble() < prob);
	}

}
