package bigmood;

import dataholder.District;
import util.DataReader;

import java.io.IOException;
import java.util.Map;

/**
 * Calculate the national shift using 2014 turnout data.
 */
public class SimpleNatlShiftCalc implements NationalShiftCalculator {

    /**
     * Map of district names to how many people voted in that district in 2014 for districts that were contested in
     * 2014.
     */
    private final Map<String, Integer> districtToVoteMap;

    /**
     * Default constructor.
     *
     * @param congressionalTurnout2014 he file with the turnout for the 2014 congressional elections.
     * @throws IOException If the turnout file is missing/improperly formatted.
     */
    public SimpleNatlShiftCalc(String congressionalTurnout2014) throws IOException {
        districtToVoteMap = DataReader.get2014Turnout(congressionalTurnout2014);
    }

    /**
     * Calculate the national shift.
     *
     * @param districts A list of every district, with the fundamental dem win percent filled in.
     * @return The national shift to apply to each district, where 0.01 is 1% in favor of the Democrats and -0.01 is 1%
     * for the Republicans.
     */
    @Override
    public NationalShiftFunction getFunction(District[] districts) {
        //Dummy variable to avoid garbage collection
        double districtVotes;

        double numerator = 0;
        double denominator = 0;
        double varianceSum = 0;

        //Find the total number of Democrat votes we expect and total number of dem votes we expect, in districts
        // where 2014 was contested.
        for (District district : districts) {
            if (districtToVoteMap.containsKey(district.getName())) {
                districtVotes = districtToVoteMap.get(district.getName()) * district.getSeerDemPercent();
                numerator += districtVotes;
                varianceSum += Math.pow(districtVotes * district.getSeerStDv(), 2);
                denominator += districtToVoteMap.get(district.getName());
            }
        }
        final double demVoteShare = numerator / denominator;
        double stDv = Math.sqrt(varianceSum) / denominator;

        //Return the difference between the generic ballot and our predicted national percent of dem votes.
        return new SimpleNationalShiftFunction(demVoteShare, stDv);
    }
}
