import java.util.Random;

public class Simulations {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
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
