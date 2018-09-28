package polistat;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class District {

    private final String state;
    private final int district;
    private final Poll[] polls;
    private final boolean repIncumbent;
    private final boolean demIncumbent;
    private final double obama2012;
    private final Double dem2014;
    private final double hillary2016;
    private final Double dem2016;
    private final double elasticity;
    private final double bantorMargin;

    private double fundamentalMargin;
    private double fundamentalStdv;
    private double nationalCorrectionMargin;
    private double nationalCorrectionStdv;
    private double finalMargin;
    private double finalStdv;

    public District(String state, int district, Poll[] polls, boolean repIncumbent,
                    boolean demIncumbent, double obama2012, Double dem2014,
                    double hillary2016, Double dem2016, double elasticity,
                    double bantorMargin) {
        this.state = state;
        this.district = district;
        this.polls = polls;
        this.repIncumbent = repIncumbent;
        this.demIncumbent = demIncumbent;
        this.obama2012 = obama2012;
        this.dem2014 = dem2014;
        this.hillary2016 = hillary2016;
        this.dem2016 = dem2016;
        this.elasticity = elasticity;
        this.bantorMargin = bantorMargin;
    }

    public String getState() {
        return state;
    }

    public int getDistrict() {
        return district;
    }

    public Poll[] getPolls() {
        return polls;
    }

    public boolean isRepIncumbent() {
        return repIncumbent;
    }

    public boolean isDemIncumbent() {
        return demIncumbent;
    }

    public double getObama2012() {
        return obama2012;
    }

    public Double getDem2014() {
        return dem2014;
    }

    public double getHillary2016() {
        return hillary2016;
    }

    public Double getDem2016() {
        return dem2016;
    }

    public double getElasticity() {
        return elasticity;
    }

    public double getBantorMargin() {
        return bantorMargin;
    }

    public double getFundamentalMargin() {
        return fundamentalMargin;
    }

    public void setFundamentalMargin(double fundamentalMargin) {
        this.fundamentalMargin = fundamentalMargin;
    }

    public double getNationalCorrectionMargin() {
        return nationalCorrectionMargin;
    }

    public void setNationalCorrectionMargin(double nationalCorrectionMargin) {
        this.nationalCorrectionMargin = nationalCorrectionMargin;
    }

    public double getFinalMargin() {
        return finalMargin;
    }

    public void setFinalMargin(double finalMargin) {
        this.finalMargin = finalMargin;
    }
    
    public double getFundamentalStdv() {
        return fundamentalStdv;
    }

    public void setFundamentalStdv(double fundamentalStdv) {
        this.fundamentalStdv = fundamentalStdv;
    }
    
    public double getNationalCorrectionStdv() {
        return nationalCorrectionStdv;
    }

    public void setNationalCorrectionStdv(double nationalCorrectionStdv) {
        this.nationalCorrectionStdv = nationalCorrectionStdv;
    }
    
    public double getFinalStdv() {
        return finalStdv;
    }

    public void setFinalStdv(double finalStdv) {
        this.finalStdv = finalStdv;
    }

    public static District[] parseFromCSV(String filename) throws FileNotFoundException {
        FileInputStream file = new FileInputStream(filename);
        while (file.)
    }
}
