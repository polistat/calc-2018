package auspice;

import dataholder.District;

/**
 * Predict Democratic votes percent using district-level polls or Blairvoyance.
 */
public abstract class AuspiceModel {

    /**
     * The method to use for averaging polls when a district has multiple polls.
     */
    protected PollAverager pollAverager;

    /**
     * Default constructor.
     *
     * @param pollAverager The method to use for averaging polls when a district has multiple polls.
     */
    public AuspiceModel(PollAverager pollAverager) {
        this.pollAverager = pollAverager;
    }

    /**
     * Calculate Democratic vote percent and standard deviation using the polls in that district or Blairvoyance. This
     * modifies the district's auspiceDemPercent and auspiceDemStDv.
     *
     * @param district A district with the bigmood dem percent and standard deviation already calculated.
     */
    public abstract void calculateAuspice(District district);

    /**
     * Calculate Democratic vote percent and standard deviation for each competitive district using polls or
     * Blairvoyance. This modifies each competitive district's auspiceDemPercent and auspiceDemStDv.
     *
     * @param districts A list of all the districts, with bigmood dem percent and standard deviation calculated.
     */
    public void calcAll(District[] districts) {
        for (District district : districts) {
            if (district.isContested()) {
                calculateAuspice(district);
            }
        }
    }
}
