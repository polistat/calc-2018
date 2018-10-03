import java.util.Random;

public class Simulations {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

    public static double[] simulate(District[] districts, int iterations) {
        double[] raceDemPercent = new double[districts.length];
        double[] raceStDv = new double[districts.length];
        for (int i = 0; i < districts.length; i++) {
            raceDemPercent[i] = districts[i].getFinalDemPercent();
            raceStDv[i] = districts[i].getFinalStDv();
        }
        double[] probs = percToProb(raceDemPercent, raceStDv, iterations);
        return probSimulate(probs, iterations);
    }

    private static double[] percToProb(double[] racePerc, double[] raceStDv, int iterations) {
        double[] raceProb = new double[racePerc.length];
        Random generator = new Random();
        for (int i = 0; i < racePerc.length; i++) {
            double demWins = 0.0;
            for (int j = 0; j < iterations; j++) {
                if (generator.nextGaussian() * raceStDv[i] + racePerc[i] > 0.5) {
                    demWins++;
                }
            }
            raceProb[i] = demWins / iterations;
        }
        return raceProb;
    }

    private static double[] probSimulate(double[] raceProb, int iterations) {
        double[] distribution = new double[raceProb.length + 1];
        for (int i = 0; i < iterations; i++) {
            int demWins = 0;
            for (int j = 0; j < raceProb.length; j++) {
                if (binGen(raceProb[j])) {
                    demWins++;
                }
            }
            distribution[demWins]++;
        }
        return distribution;
    }

    private static boolean binGen(double prob) {
        Random generator = new Random();
        return (generator.nextDouble() < prob);
    }

}
