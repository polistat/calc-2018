import java.time.LocalDate;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;


public class Simulations {


	public static void main(String[] args) throws IOException {
		
	}
	
	public static void write(District[] districts) throws IOException {
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("/Users/stevenqu/Documents/12th Grade/PoliStat/outputP.txt")));
		// output: margin with stdv, probability of winning, histogram
		
		double[] racePerc = new double[districts.length];
		double[] raceStdv = new double[districts.length];
		for (int i = 0; i < districts.length; i++) {
			racePerc[i] = districts[i].getFinalMargin();
			raceStdv[i] = districts[i].getFinalStdv();
		}
		
		int sampleNormal = 1000;
		int makeHisto = 10000;
		double[] probs = percToProb(racePerc, raceStdv, sampleNormal);
		double[] histo = probSimulate(probs, makeHisto);
		
		LocalDate today = LocalDate.now();
		
		for (int i = 0; i < districts.length; i++) {
			out.println(today.getYear() + "," + today.getMonth() + "," + 
					today.getDayOfMonth() + "," +racePerc[i] + "," + 
					raceStdv[i] + "," + probs[i]);
		}
		for (int i = 0; i < histo.length; i++) {
			out.println((histo[i] / makeHisto));
		}
		
	}
	
	
	// unnecessary
	public static double[] simulate(District[] districts) {
		double[] racePerc = new double[districts.length];
		double[] raceStdv = new double[districts.length];
		for (int i = 0; i < districts.length; i++) {
			racePerc[i] = districts[i].getFinalMargin();
			raceStdv[i] = districts[i].getFinalStdv();
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
