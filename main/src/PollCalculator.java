public abstract class PollCalculator {

    protected PollAverager pollAverager;

    public PollCalculator(PollAverager pollAverager) {
        this.pollAverager = pollAverager;
    }

    public abstract double calculatePolls(District district);

    public void calcAll(District[] districts) {
        for (District district : districts) {
            if (district.isContested()) {
                calculatePolls(district);
            }
        }
    }
}
