package dataholder;

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
     * Whether the Republican running in this district is an incumbent.
     */
    private final boolean repIncumbent;

    /**
     * Whether the Democrat running in this district is an incumbent.
     */
    private final boolean demIncumbent;

    /**
     * The MARGIN of the two-party vote Obama got in this district in 2012, from -1 to 1.
     */
    private final double obama2012;

    /**
     * The MARGIN of the two-party vote the Democratic congressional candidate got in this district in 2014, from -1 to
     * 1, or null if the 2014 race wasn't contested.
     */
    private final Double dem2014;

    /**
     * The MARGIN of the two-party vote Hillary got in this district in 2016, from -1 to 1.
     */
    private final double hillary2016;

    /**
     * The MARGIN of the two-party vote the Democratic congressional candidate got in this district in 2016, from -1 to
     * 1, or null if the 2014 race wasn't contested.
     */
    private final Double dem2016;

    /**
     * 538's elasticity score for this district, representing how much it's affected by changes in the national mood.
     */
    private final double elasticity;

    /**
     * The percent of the two-party vote Democrats will get in this district according to Blairvoyance, from 0 to 1, or
     * null if Blairvoyance doesn't have a prediction.
     */
    private final Double blairvoyanceDemPercent;

    /**
     * The weight assigned to this district based on how well Blairvoyance can predict the results. Because Blairvoyance
     * is based on polling, which happens in close districts, this number is larger for closer districts.
     */
    private final Double blairvoyanceWeight;

    /**
     * Whether a Republican and a Democrat are running against each other in this district's general election. A
     * district where no Democrat or Republican has been nominated is not contested, and neither is a race between two
     * members of the same party in a top-two primary district.
     */
    private final boolean contested;
    /**
     * Whether there were Democrat/Republican incumbents in past elections.
     */
    private final int demInc14, repInc14, demInc16, repInc16;
    /**
     * The predicted percent of the two-party vote the Democrats will win in this district, according to the SEER
     * model.
     */
    private double seerDemPercent;
    /**
     * The standard deviation of the SEER model's prediction.
     */
    private double seerStDv;
    /**
     * The predicted percent of the two-party vote the Democrats will win in this district, according to the SEER model
     * corrected for the national mood.
     */
    private double bigmoodDemPercent;
    /**
     * The standard deviation of the SEER model's prediction, corrected for the national mood.
     */
    private double bigmoodStDv;
    /**
     * The AUSPICE prediction for what percent of the two-party vote the Democrats will win in this district.
     */
    private double auspiceDemPercent;
    /**
     * The standard deviation of the AUSPICE prediction.
     */
    private double auspiceStDv;

    /**
     * Default constructor.
     *
     * @param name                   The name of this district, in the format ST-##, where ST is the state's postal code
     *                               and ## is the district's 2-digit number, e.g. 01 for the first district.
     * @param polls                  A list of all the polls taken in this district.
     * @param repIncumbent           Whether the Republican running in this district is an incumbent.
     * @param demIncumbent           Whether the Democrat running in this district is an incumbent.
     * @param obama2012              The MARGIN of the two-party vote Obama got in this district in 2012, from -1 to 1.
     * @param dem2014                The MARGIN of the two-party vote the Democratic congressional candidate got in this
     *                               district in 2014, from -1 to 1, or null if the 2014 race wasn't contested.
     * @param hillary2016            The MARGIN of the two-party vote Hillary got in this district in 2016, from -1 to
     *                               1.
     * @param dem2016                The MARGIN of the two-party vote the Democratic congressional candidate got in this
     *                               district in 2016, from -1 to 1, or null if the 2014 race wasn't contested.
     * @param elasticity             538's elasticity score for this district, representing how much it's affected by
     *                               changes in the national mood.
     * @param blairvoyanceDemPercent The percent of the two-party vote Democrats will get in this district according to
     *                               Blairvoyance, from 0 to 1, or null if Blairvoyance doesn't have a prediction.
     * @param blairvoyanceWeight     The weight assigned to this district based on how well Blairvoyance can predict the
     *                               results. Because Blairvoyance is based on polling, which happens in close
     *                               districts, this number is larger for closer districts.
     * @param repRunning             Whether a Republican is running in this district's general election.
     * @param demRunning             Whether a Democrat is running in this district's general election.
     * @param demInc14               Whether a Democrat was incumbent in this district in 2014.
     * @param repInc14               Whether a Republican was incumbent in this district in 2014.
     * @param demInc16               Whether a Democrat was incumbent in this district in 2016.
     * @param repInc16               Whether a Republican was incumbent in this district in 2016.
     */
    public District(String name, Poll[] polls, boolean repIncumbent,
                    boolean demIncumbent, double obama2012, Double dem2014,
                    double hillary2016, Double dem2016, double elasticity,
                    Double blairvoyanceDemPercent, Double blairvoyanceWeight, boolean repRunning, boolean demRunning,
                    int demInc14, int repInc14, int demInc16, int repInc16) {
        this.name = name;
        this.polls = polls;
        this.repIncumbent = repIncumbent;
        this.demIncumbent = demIncumbent;
        this.obama2012 = obama2012;
        this.dem2014 = dem2014;
        this.hillary2016 = hillary2016;
        this.dem2016 = dem2016;
        this.demInc14 = demInc14;
        this.repInc14 = repInc14;
        this.demInc16 = demInc16;
        this.repInc16 = repInc16;
        this.elasticity = elasticity;
        this.blairvoyanceDemPercent = blairvoyanceDemPercent;
        this.blairvoyanceWeight = blairvoyanceWeight;
        this.contested = repRunning && demRunning;

        if (!contested) {
            //Set generic and final percents, but not fundamentals because we use them to calculate shift.
            this.bigmoodStDv = 0;
            this.auspiceStDv = 0;
            if (!repRunning) {
                this.bigmoodDemPercent = 1;
                this.auspiceDemPercent = 1;
            } else {
                this.bigmoodDemPercent = 0;
                this.auspiceDemPercent = 0;
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
     * @return Whether the Republican running in this district is an incumbent.
     */
    public boolean isRepIncumbent() {
        return repIncumbent;
    }

    /**
     * @return Whether the Democrat running in this district is an incumbent.
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
     * @return The MARGIN of the two-party vote the Democratic congressional candidate got in this district in 2014,
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
     * @return The MARGIN of the two-party vote the Democratic congressional candidate got in this district in 2016,
     * from -1 to 1, or null if the 2014 race wasn't contested.
     */
    public Double getDem2016() {
        return dem2016;
    }

    /**
     * @return 1 if a Democrat was incumbent in this district in 2014, 0 otherwise.
     */
    public int getDemInc14() {
        return demInc14;
    }

    /**
     * @return 1 if a Republican was incumbent in this district in 2014, 0 otherwise.
     */
    public int getRepInc14() {
        return repInc14;
    }

    /**
     * @return 1 if a Democrat was incumbent in this district in 2016, 0 otherwise.
     */
    public int getDemInc16() {
        return demInc16;
    }

    /**
     * @return 1 if a Republican was incumbent in this district in 2016, 0 otherwise.
     */
    public int getRepInc16() {
        return repInc16;
    }

    /**
     * @return 538's elasticity score for this district, representing how much it's affected by changes in the national
     * mood.
     */
    public double getElasticity() {
        return elasticity;
    }

    /**
     * @return The percent of the two-party vote Democrats will get in this district according to Blairvoyance, from 0
     * to 1, or null if Blairvoyance doesn't have a prediction.
     */
    public Double getBlairvoyanceDemPercent() {
        return blairvoyanceDemPercent;
    }

    /**
     * @return The weight assigned to this district based on how well Blairvoyance can predict the results. Because
     * Blairvoyance is based on polling, which happens in close districts, this number is larger for closer districts.
     */
    public Double getBlairvoyanceWeight() {
        return blairvoyanceWeight;
    }

    /**
     * @return The predicted percent of the two-party vote the Democrats will win in this district, according to the
     * SEER model.
     */
    public double getSeerDemPercent() {
        return seerDemPercent;
    }

    /**
     * @param seerDemPercent The predicted percent of the two-party vote the Democrats will win in this district,
     *                       according to the SEER model.
     */
    public void setSeerDemPercent(double seerDemPercent) {
        this.seerDemPercent = seerDemPercent;
    }

    /**
     * @return The predicted percent of the two-party vote the Democrats will win in this district, according to the
     * SEER model corrected for the national mood.
     */
    public double getBigmoodDemPercent() {
        return bigmoodDemPercent;
    }

    /**
     * @param bigmoodDemPercent The predicted percent of the two-party vote the Democrats will win in this district,
     *                          according to the SEER model corrected for the national mood.
     */
    public void setBigmoodDemPercent(double bigmoodDemPercent) {
        this.bigmoodDemPercent = bigmoodDemPercent;
    }

    /**
     * @return The AUSPICE prediction for what percent of the two-party vote the Democrats will win in this district.
     */
    public double getAuspiceDemPercent() {
        return auspiceDemPercent;
    }

    /**
     * @param auspiceDemPercent The AUSPICE prediction for what percent of the two-party vote the Democrats will win in
     *                          this district.
     */
    public void setAuspiceDemPercent(double auspiceDemPercent) {
        this.auspiceDemPercent = auspiceDemPercent;
    }

    /**
     * @return The standard deviation of the SEER model's prediction.
     */
    public double getSeerStDv() {
        return seerStDv;
    }

    /**
     * @param seerStDv The standard deviation of the SEER model's prediction.
     */
    public void setSeerStDv(double seerStDv) {
        this.seerStDv = seerStDv;
    }

    /**
     * @return The standard deviation of the SEER model's prediction, corrected for the national mood.
     */
    public double getBigmoodStDv() {
        return bigmoodStDv;
    }

    /**
     * @param bigmoodStDv The standard deviation of the SEER model's prediction, corrected for the national mood.
     */
    public void setBigmoodStDv(double bigmoodStDv) {
        this.bigmoodStDv = bigmoodStDv;
    }

    /**
     * @return The standard deviation of the AUSPICE prediction.
     */
    public double getAuspiceStDv() {
        return auspiceStDv;
    }

    /**
     * @param auspiceStDv The standard deviation of the AUSPICE prediction.
     */
    public void setAuspiceStDv(double auspiceStDv) {
        this.auspiceStDv = auspiceStDv;
    }

    /**
     * @return Whether this district has any polls.
     */
    public boolean hasPolls() {
        return polls != null && !(polls.length == 0);
    }

    /**
     * @return Whether a Republican and a Democrat are running against each other in this district's general election. A
     * district where no Democrat or Republican has been nominated is not contested, and neither is a race between two
     * members of the same party in a top-two primary district.
     */
    public boolean isContested() {
        return contested;
    }

    @Override
    public String toString() {
        return getName() + ", polls: " + this.hasPolls() + ", dem2014: " + this.getDem2014() + ", dem2016: " + this.getDem2016()
                + ", Obama: " + this.getObama2012() + ", Hillary: " + this.getHillary2016() + ", fundamental dem %: " +
                this.getSeerDemPercent() + ", final dem %: " + this.getAuspiceDemPercent();
    }
}
