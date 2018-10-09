package auspice;

import dataholder.District;
import dataholder.Grade;
import dataholder.Poll;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PollStDvShifter {

    public static void shiftPolls(District[] districts, double[] avgGradeStDvs){
        List<Poll> allDistrictPolls = new ArrayList<>();
        for (District district : districts){
            if (district.hasPolls()) {
                allDistrictPolls.addAll(Arrays.asList(district.getPolls()));
            }
        }

        double[] stDvByGrade = new double[4];
        double[] numPollsByGrade = new double[4];
        double[] shiftByGrade = new double[4];
        for (Poll poll : allDistrictPolls){
            stDvByGrade[Grade.indexGrade(poll.getGrade())] += poll.getTheoreticalStandardDeviation();
            numPollsByGrade[Grade.indexGrade(poll.getGrade())]++;
        }
        for (int i = 0; i < 4; i++){
            stDvByGrade[i] /= numPollsByGrade[i];
            shiftByGrade[i] = avgGradeStDvs[i] - stDvByGrade[i];
        }
        System.out.println(Arrays.toString(shiftByGrade));
        for (Poll poll : allDistrictPolls){
            poll.setStandardDeviation(poll.getTheoreticalStandardDeviation() +
                    shiftByGrade[Grade.indexGrade(poll.getGrade())]);
        }
    }
}
