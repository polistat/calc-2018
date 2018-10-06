public abstract class FundamentalCalculator {

    public abstract double calcFundamentalDemPercent(District district);

    public abstract double calcFundamentalStDv(District district);

    public void calcAll(District[] districts) {
        for (District district : districts) {
            calcFundamentalDemPercent(district);
            calcFundamentalStDv(district);
        }
    }
}
