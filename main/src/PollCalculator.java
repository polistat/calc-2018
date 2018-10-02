public abstract class PollCalculator {

    protected PollAverager pollAverager;

    public PollCalculator(PollAverager pollAverager){
        this.pollAverager = pollAverager;
    }

    public abstract double calculatePolls(District district);
}
