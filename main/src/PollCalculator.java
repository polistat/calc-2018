/**
 * Predict democratic votes percent using district-level polls or Blairvoyance.
 */
public abstract class PollCalculator {

    /**
     * The method to use for averaging polls when a district has multiple polls.
     */
    protected PollAverager pollAverager;

    /**
     * Default constructor.
     *
     * @param pollAverager The method to use for averaging polls when a district has multiple polls.
     */
    public PollCalculator(PollAverager pollAverager) {
        this.pollAverager = pollAverager;
    }

    /**
     * Calculate democratic vote percent and standard deviation using the polls in that district or Blairvoyance. This
     * modifies the district's finalDemPercent and finalDemStDv.
     *
     * @param district A district with the generic-corrected average dem percent and standard deviation already
     *                 calculated.
     */
    public abstract void calculatePolls(District district);

    /**
     * Calculate democratic vote percent and standard deviation for each competitive district using polls or
     * Blairvoyance. This modifies each competitive district's finalDemPercent and finalDemStDv.
     *
     * @param districts A list of all the districts, with generic-corrected average dem percent and standard deviation
     *                  calculated.
     */
    public void calcAll(District[] districts) {
        for (District district : districts) {
            if (district.isContested()) {
                calculatePolls(district);
            }
        }
    }
}
