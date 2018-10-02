public class LinearFundamentalCalculator implements FundamentalCalculator {
	
	private final double obama2012weight;
	private final double dem2014weight;
	private final double hillary2016weight;
	private final double dem2016weight;
	
	private final double partisanshipWeight;
	private final double demIncWeight;
	private final double repIncWeight;
	
	private final double incStdv;
	private final double openStdv;
	
	public LinearFundamentalCalculator(double obama2012weight, double dem2014weight, double hillary2016weight,
			double dem2016weight, double partisanshipWeight, double demIncWeight, 
			double repIncWeight, double incStdv, double openStdv) {
		this.obama2012weight = obama2012weight;
		this.dem2014weight = dem2014weight;
		this.hillary2016weight = hillary2016weight;
		this.dem2016weight = dem2016weight;
		this.partisanshipWeight = partisanshipWeight;
		this.demIncWeight = demIncWeight;
		this.repIncWeight = repIncWeight;
		this.incStdv = incStdv;
		this.openStdv = openStdv;
	}
	
	
	@Override
	public double calcFundamentalMargin(District district) {
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
            predictedDemMargin += this.demIncWeight;
        }
        if (district.isRepIncumbent()) {
            predictedDemMargin += this.repIncWeight;
        }
		
		district.setFundamentalMargin(predictedDemMargin);
		return predictedDemMargin;
	}
	
	public double calcFundamentalStdv(District district) {
		if (district.isDemIncumbent() || district.isRepIncumbent()) {
			district.setFundamentalStdv(this.incStdv);
			return this.incStdv;
		}
		else {
			district.setFundamentalStdv(this.openStdv);
			return this.openStdv;
		}
	}


}
