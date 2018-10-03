public class LinearFundamentalCalculator implements FundamentalCalculator {
	
	private final double obama2012weight;
	private final double dem2014weight;
	private final double hillary2016weight;
	private final double dem2016weight;
	
	private final double partisanshipWeight;
	private final double demIncumbentWeight;
	private final double repIncumbentWeight;
	
	private final double incumbentStDv;
	private final double openStDv;
	
	public LinearFundamentalCalculator(double obama2012weight, double dem2014weight, double hillary2016weight,
									   double dem2016weight, double partisanshipWeight, double demIncumbentWeight,
									   double repIncumbentWeight, double incumbentStDv, double openStDv) {
		this.obama2012weight = obama2012weight;
		this.dem2014weight = dem2014weight;
		this.hillary2016weight = hillary2016weight;
		this.dem2016weight = dem2016weight;
		this.partisanshipWeight = partisanshipWeight;
		this.demIncumbentWeight = demIncumbentWeight;
		this.repIncumbentWeight = repIncumbentWeight;
		this.incumbentStDv = incumbentStDv;
		this.openStDv = openStDv;
	}
	
	
	@Override
	public double calcFundamentalDemPercent(District district) {
		double numerator = obama2012weight*district.getObama2012() + hillary2016weight*district.getHillary2016();
		double denominator = obama2012weight + hillary2016weight;

		if (district.getDem2014() != null) {
			numerator += dem2014weight*district.getDem2014();
			denominator += dem2014weight;
		}

		if (district.getDem2016() != null) {
			numerator += dem2016weight*district.getDem2016();
			denominator += dem2016weight;
		}
		
		double BPI = numerator/denominator;
		double predictedDemMargin = BPI * this.partisanshipWeight;

		//Not else if because of pennsylvania I think
		if (district.isDemIncumbent()) {
            predictedDemMargin += this.demIncumbentWeight;
        }
        if (district.isRepIncumbent()) {
            predictedDemMargin += this.repIncumbentWeight;
        }
		
		district.setFundamentalDemPercent(0.5 + 0.5*predictedDemMargin);
		return 0.5 + 0.5*predictedDemMargin;
	}
	
	public double calcFundamentalStDv(District district) {
		if (district.isDemIncumbent() || district.isRepIncumbent()) {
			district.setFundamentalStDv(this.incumbentStDv);
			return this.incumbentStDv;
		}
		else {
			district.setFundamentalStDv(this.openStDv);
			return this.openStDv;
		}
	}


}
