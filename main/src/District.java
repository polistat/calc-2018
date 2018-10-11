/**
 * A representation of a single congressional district.
 */
public class District {

    /**
     * The name of this district, in the format ST-##, where ST is the state's postal code and ## is the district's
     * 2-digit number, e.g. 01 for the first district.
     */
    private final String name;

    /**
     * A list of all the polls taken in this district.
     */
    private final Poll[] polls;

    /**
     * Whether the republican running in this district is an incumbent.
     */
    private final boolean repIncumbent;

    /**
     * Whether the democrat running in this district is an incumbent.
     */
    private final boolean demIncumbent;

    /**
     * The MARGIN of the two-party vote Obama got in this district in 2012, from -1 to 1.
     */
    private final double obama2012;

    /**
     * The MARGIN of the two-party vote the democratic congressional candidate got in this district in 2014, from -1 to
     * 1, or null if the 2014 race wasn't contested.
     */
    private final Double dem2014;

    /**
     * The MARGIN of the two-party vote Hillary got in this district in 2016, from -1 to 1.
     */
    private final double hillary2016;

    /**
     * The MARGIN of the two-party vote the democratic congressional candidate got in this district in 2016, from -1 to
     * 1, or null if the 2014 race wasn't contested.
     */
    private final Double dem2016;

    /**
     * 538's elasticity score for this district, representing how much it's affected by changes in the national mood.
     */
    private final double elasticity;

    /**
     * The percent of the two-party vote democrats will get in this district according to Blairvoyance, from 0 to 1, or
     * null if Blairvoyance doesn't have a prediction.
     */
    private final Double blairvoyanceDemPercent;

    /**
     * Whether a republican and a democrat are running against each other in this district's general election. A
     * district where no dem or republican has been nominated is not contested, and neither is a race between two
     * members of the same party in a top-two primary district.
     */
    private final boolean contested;

    /**
     * The predicted percent of the two-party vote the democrats will win in this district, according to the
     * fundamentals model.
     */
    private double fundamentalDemPercent;

    /**
     * The standard deviation of the fundamentals model's prediction.
     */
    private double fundamentalStDv;

    /**
     * The predicted percent of the two-party vote the democrats will win in this district, according to the
     * fundamentals model corrected for the national mood.
     */
    private double genericCorrectedDemPercent;

    /**
     * The standard deviation of the fundamentals model's prediction, corrected for the national mood.
     */
    private double genericCorrectedStDv;

    /**
     * The final prediction for what percent of the two-party vote the democrats will win in this district.
     */
    private double finalDemPercent;

    /**
     * The standard deviation of the final prediction.
     */
    private double finalStDv;
    
    private int dInc14, rInc14, dInc16, rInc16;

    /**
     * Default constructor.
     *
     * @param name                   The name of this district, in the format ST-##, where ST is the state's postal code
     *                               and ## is the district's 2-digit number, e.g. 01 for the first district.
     * @param polls                  A list of all the polls taken in this district.
     * @param repIncumbent           Whether the republican running in this district is an incumbent.
     * @param demIncumbent           Whether the democrat running in this district is an incumbent.
     * @param obama2012              The MARGIN of the two-party vote Obama got in this district in 2012, from -1 to 1.
     * @param dem2014                The MARGIN of the two-party vote the democratic congressional candidate got in this
     *                               district in 2014, from -1 to 1, or null if the 2014 race wasn't contested.
     * @param hillary2016            The MARGIN of the two-party vote Hillary got in this district in 2016, from -1 to
     *                               1.
     * @param dem2016                The MARGIN of the two-party vote the democratic congressional candidate got in this
     *                               district in 2016, from -1 to 1, or null if the 2014 race wasn't contested.
     * @param elasticity             538's elasticity score for this district, representing how much it's affected by
     *                               changes in the national mood.
     * @param blairvoyanceDemPercent The percent of the two-party vote democrats will get in this district according to
     *                               Blairvoyance, from 0 to 1, or null if Blairvoyance doesn't have a prediction.
     * @param repRunning             Whether a republican is running in this district's general election.
     * @param demRunning             Whether a democrat is running in this district's general election.
     */
    public District(String name, Poll[] polls, boolean repIncumbent,
                     boolean demIncumbent, double obama2012, Double dem2014,
                     double hillary2016, Double dem2016, double elasticity,
                     Double blairvoyanceDemPercent, boolean repRunning, boolean demRunning,
                     int dInc14, int rInc14, int dInc16, int rInc16) {
        this.name = name;
        this.polls = polls;
        this.repIncumbent = repIncumbent;
        this.demIncumbent = demIncumbent;
        this.obama2012 = obama2012;
        this.dem2014 = dem2014;
        this.hillary2016 = hillary2016;
        this.dem2016 = dem2016;
        this.dInc14 = dInc14;
        this.rInc14 = rInc14;
        this.dInc16 = dInc16;
        this.rInc16 = rInc16;
        this.elasticity = elasticity;
        this.blairvoyanceDemPercent = blairvoyanceDemPercent;
        this.contested = repRunning && demRunning;

        if (!contested) {
            //Set generic and final percents, but not fundamentals because we use them to calculate shift.
            this.genericCorrectedStDv = 0;
            this.finalStDv = 0;
            if (!repRunning) {
                this.genericCorrectedDemPercent = 1;
                this.finalDemPercent = 1;
            } else {
                this.genericCorrectedDemPercent = 0;
                this.finalDemPercent = 0;
            }
        }
    }

    /**
     * @return The name of this district, in the format ST-##, where ST is the state's postal code and ## is the
     * district's 2-digit number, e.g. 01 for the first district.
     */
    public String getName() {
        return name;
    }

    /**
     * @return A list of all the polls taken in this district.
     */
    public Poll[] getPolls() {
        return polls;
    }

    /**
     * @return Whether the republican running in this district is an incumbent.
     */
    public boolean isRepIncumbent() {
        return repIncumbent;
    }

    /**
     * @return Whether the democrat running in this district is an incumbent.
     */
    public boolean isDemIncumbent() {
        return demIncumbent;
    }

    /**
     * @return The MARGIN of the two-party vote Obama got in this district in 2012, from -1 to 1.
     */
    public double getObama2012() {
        return obama2012;
    }

    /**
     * @return The MARGIN of the two-party vote the democratic congressional candidate got in this district in 2014,
     * from -1 to 1, or null if the 2014 race wasn't contested.
     */
    public Double getDem2014() {
        return dem2014;
    }

    /**
     * @return The MARGIN of the two-party vote Hillary got in this district in 2016, from -1 to 1.
     */
    public double getHillary2016() {
        return hillary2016;
    }

    /**
     * @return The MARGIN of the two-party vote the democratic congressional candidate got in this district in 2016,
     * from -1 to 1, or null if the 2014 race wasn't contested.
     */
    public Double getDem2016() {
        return dem2016;
    }

    public int getdInc14() {
		return dInc14;
	}

	public int getrInc14() {
		return rInc14;
	}

	public int getdInc16() {
		return dInc16;
	}

	public int getrInc16() {
		return rInc16;
	}

	/**
     * @return 538's elasticity score for this district, representing how much it's affected by changes in the national
     * mood.
     */
    public double getElasticity() {
        return elasticity;
    }

    /**
     * @return The percent of the two-party vote democrats will get in this district according to Blairvoyance, from 0
     * to 1, or null if Blairvoyance doesn't have a prediction.
     */
    public Double getBlairvoyanceDemPercent() {
        return blairvoyanceDemPercent;
    }

    /**
     * @return The predicted percent of the two-party vote the democrats will win in this district, according to the
     * fundamentals model.
     */
    public double getFundamentalDemPercent() {
        return fundamentalDemPercent;
    }

    /**
     * @param fundamentalDemPercent The predicted percent of the two-party vote the democrats will win in this district,
     *                              according to the fundamentals model.
     */
    public void setFundamentalDemPercent(double fundamentalDemPercent) {
        this.fundamentalDemPercent = fundamentalDemPercent;
    }

    /**
     * @return The predicted percent of the two-party vote the democrats will win in this district, according to the
     * fundamentals model corrected for the national mood.
     */
    public double getGenericCorrectedDemPercent() {
        return genericCorrectedDemPercent;
    }

    /**
     * @param genericCorrectedDemPercent The predicted percent of the two-party vote the democrats will win in this
     *                                   district, according to the fundamentals model corrected for the national mood.
     */
    public void setGenericCorrectedDemPercent(double genericCorrectedDemPercent) {
        this.genericCorrectedDemPercent = genericCorrectedDemPercent;
    }

    /**
     * @return The final prediction for what percent of the two-party vote the democrats will win in this district.
     */
    public double getFinalDemPercent() {
        return finalDemPercent;
    }

    /**
     * @param finalDemPercent The final prediction for what percent of the two-party vote the democrats will win in this
     *                        district.
     */
    public void setFinalDemPercent(double finalDemPercent) {
        this.finalDemPercent = finalDemPercent;
    }

    /**
     * @return The standard deviation of the fundamentals model's prediction.
     */
    public double getFundamentalStDv() {
        return fundamentalStDv;
    }

    /**
     * @param fundamentalStDv The standard deviation of the fundamentals model's prediction.
     */
    public void setFundamentalStDv(double fundamentalStDv) {
        this.fundamentalStDv = fundamentalStDv;
    }

    /**
     * @return The standard deviation of the fundamentals model's prediction, corrected for the national mood.
     */
    public double getGenericCorrectedStDv() {
        return genericCorrectedStDv;
    }

    /**
     * @param genericCorrectedStDv The standard deviation of the fundamentals model's prediction, corrected for the
     *                             national mood.
     */
    public void setGenericCorrectedStDv(double genericCorrectedStDv) {
        this.genericCorrectedStDv = genericCorrectedStDv;
    }

    /**
     * @return The standard deviation of the final prediction.
     */
    public double getFinalStDv() {
        return finalStDv;
    }

    /**
     * @param finalStDv The standard deviation of the final prediction.
     */
    public void setFinalStDv(double finalStDv) {
        this.finalStDv = finalStDv;
    }

    /**
     * @return Whether this district has any polls.
     */
    public boolean hasPolls() {
        return polls != null && !(polls.length == 0);
    }

    /**
     * @return Whether a republican and a democrat are running against each other in this district's general election. A
     * district where no dem or republican has been nominated is not contested, and neither is a race between two
     * members of the same party in a top-two primary district.
     */
    public boolean isContested() {
        return contested;
    }

    @Override
    public String toString() {
        return getName() + ", polls: " + this.hasPolls() + ", dem2014: " + this.getDem2014() + ", dem2016: " + this.getDem2016()
                + ", Obama: " + this.getObama2012() + ", Hillary: " + this.getHillary2016() + ", fundamental dem %: " +
                this.getFundamentalDemPercent() + ", final dem %: " + this.getFinalDemPercent();
    }
}
