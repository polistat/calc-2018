package polistat;

import java.util.Random;

public class Simulations {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public static double[] percToProb(double[] racePerc, double[] raceStd) {
		double[] raceProb = new double[racePerc.length];
		Random generator = new Random();
		int iter = 1000000;
		for (int i = 0; i < racePerc.length; i++) {
			double demWins = 0.0;
			for (int j = 0; j < iter; j++) {
				if (generator.nextGaussian() * raceStd[i] + racePerc[i] > 0.5) {
					demWins++;
				}
			}
			raceProb[i] = demWins / iter;
		}
		return raceProb;
	}
	
	public static double[] simulate(double[] raceProb, int iter) {
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
