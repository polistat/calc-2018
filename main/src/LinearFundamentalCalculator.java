public class LinearFundamentalCalculator implements FundamentalCalculator {
	
	private final double obama2012avg;
	private final Double dem2014avg;
	private final double hillary2016avg;
	private final Double dem2016avg;
	
	private final double nationalMarg;
	
	private final double obama2012weight;
	private final double dem2014weight;
	private final double hillary2016weight;
	private final double dem2016weight;
	
	private final double partisanshipWeight;
	private final double demIncWeight;
	private final double repIncWeight;
	
	private final double incStdv;
	private final double openStdv;
	
	public LinearFundamentalCalculator(double obama2012avg, Double dem2014avg,
			double hillary2016avg, Double dem2016avg, double nationalMarg,
			double obama2012weight, double dem2014weight, double hillary2016weight, 
			double dem2016weight, double partisanshipWeight, double demIncWeight, 
			double repIncWeight, double incStdv, double openStdv) {
		this.obama2012avg = obama2012avg;
		this.dem2014avg = dem2014avg;
		this.hillary2016avg = hillary2016avg;
		this.dem2016avg = dem2016avg;
		this.nationalMarg = nationalMarg;
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
		
		double tempObama2012weight = this.obama2012weight;
		double tempDem2014weight = this.dem2014weight;
		double tempHillary2016weight = this.hillary2016weight;
		double tempDem2016weight = this.dem2016weight;
		
		double relObama2012 = district.getObama2012() - this.obama2012avg;
		
		Double relDem2014;
		if (district.getDem2014() == null) {
			tempDem2014weight = 0.0;
			relDem2014 = 0.0;
		}
		else {
			relDem2014 = district.getDem2014() - this.dem2014avg;
		}
		
		double relHillary2016 = district.getHillary2016() - this.hillary2016avg;
		
		Double relDem2016;
		if (district.getDem2016() == null) {
			tempDem2016weight = 0.0;
			relDem2016 = 0.0;
		}
		else {
			relDem2016 = district.getDem2016() - this.dem2016avg;
		}
		
		double BPI = (relObama2012 * tempObama2012weight + relDem2014 * tempDem2014weight +
				relHillary2016 * tempHillary2016weight + relDem2016 * tempDem2016weight) / 
				(tempObama2012weight + tempDem2014weight + tempHillary2016weight + tempDem2016weight);
		double predictedDemMargin = BPI * this.partisanshipWeight;
		if (district.isDemIncumbent()) predictedDemMargin += this.demIncWeight;
		else if (district.isRepIncumbent()) predictedDemMargin += this.repIncWeight;
		
		double demMargin = 0.5 + 0.5 * predictedDemMargin;
		
		district.setFundamentalMargin(demMargin);
		return demMargin;
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
