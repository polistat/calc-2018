package bigmood;

import dataholder.District;
import util.DataReader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Calculate the national shift using Daniel Zhu's method of interpolating with
 * 2016 turnout data.
 */
public class DZhuNatlShiftCalc implements NationalShiftCalculator {

	/**
	 * Map of district names to how many people voted in that district in 2014, plus
	 * interpolation for districts that weren't contested in 2014.
	 */
	private final Map<String, Integer> districtToVoteMap;

	/**
	 * Default constructor.
	 *
	 * @param congressionalTurnout2014 The file with the turnout for the 2014
	 *                                 congressional elections.
	 * @param congressionalTurnout2016 The file with the turnout for the 2016
	 *                                 congressional elections.
	 * @param redistrict2018           The set of postal codes for the states that
	 *                                 were redistricted between 2016 and 2018.
	 * @param redistrict2016           The set of postal codes for the states that
	 *                                 were redistricted between 2014 and 2016.
	 * @throws IOException If either turnout file is missing/improperly formatted.
	 */
	public DZhuNatlShiftCalc(String congressionalTurnout2014, String congressionalTurnout2016,
			Set<String> redistrict2018, Set<String> redistrict2016) throws IOException {
		Map<String, Integer> districtToVoteMap2016 = DataReader.get2016Turnout(congressionalTurnout2016);
		int total2016 = districtToVoteMap2016.values().stream().mapToInt(Integer::intValue).sum();
		int dist2016 = districtToVoteMap2016.size();

		Map<String, Integer> districtToVoteMap2014 = DataReader.get2014Turnout(congressionalTurnout2014);
		int total2014 = districtToVoteMap2016.values().stream().mapToInt(Integer::intValue).sum();
		int dist2014 = districtToVoteMap2016.size();

		double conversion = (double) total2014 * dist2016 / dist2014 / total2016;
		int average2014 = total2014 / dist2014;

		districtToVoteMap = new HashMap<>();
		for (String district : districtToVoteMap2016.keySet()) {
			int turnout;
			String state = district.substring(0, 2);
			if (redistrict2018.contains(state)) {
				turnout = average2014;
			} else if (redistrict2016.contains(state)) {
				turnout = (int) (conversion * districtToVoteMap2016.getOrDefault(district, average2014));
			} else {
				turnout = districtToVoteMap2014.getOrDefault(district,
						districtToVoteMap2016.getOrDefault(district, average2014));
			}
			districtToVoteMap.put(district, turnout);
		}
	}

	/**
	 * Calculate the national shift.
	 *
	 * @param districts         A list of every district, with the fundamental dem
	 *                          win percent filled in.
	 * @return The national shift to apply to each district, where 0.01 is 1% in
	 *         favor of the democrats and -0.01 is 1% for the republicans.
	 */
	@Override
	public NationalShiftFunction getFunction(District[] districts) {
		double numerator = 0;
		double denominator = 0;
		double varianceSum = 0;

		// Find the total number of democrat votes we expect and total number of dem
		// votes we expect
		for (District district : districts) {
			numerator += districtToVoteMap.get(district.getName()) * district.getFundamentalDemPercent();
			varianceSum += Math.pow(districtToVoteMap.get(district.getName()) * district.getFundamentalStDv(), 2);
			denominator += districtToVoteMap.get(district.getName());
		}

		double demBaseline = numerator / denominator;
		double stDv = Math.sqrt(varianceSum)/denominator;

		// Return the difference between the generic ballot and our predicted national
		// percent of dem votes.
		return new SimpleNationalShiftFunction(demBaseline, stDv);
	}
}
