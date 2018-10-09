

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataReader {

    /**
     * Read a set of national generic ballot polls from a file.
     *
     * @param filename The file with the poll data.
     * @return A list of polls corresponding to the data in the file.
     * @throws IOException If the file is missing or improperly formatted
     */
    public static Poll[] readNationalPolls(String filename) throws IOException {
        //Define line out here to avoid garbage collection.
        String line;
        List<Poll> polls = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        //Clear header line
        reader.readLine();
        while ((line = reader.readLine()) != null) {
            String[] commaSplit = line.split(",");
            //File must be formatted as follows:
            //final date the poll was taken,dem percent,rep percent,sample size,which voter model was used,pollster
            // lean,pollster grade,pollster name
            LocalDate date = LocalDate.parse(commaSplit[0], DateTimeFormatter.ofPattern("M/d/yyyy"));
            double rawDemPercent = Double.parseDouble(commaSplit[1]);
            double rawRepPercent = Double.parseDouble(commaSplit[2]);
            double sampleSize = Double.parseDouble(commaSplit[3]);
            Poll.VoterModel voterModel = Poll.VoterModel.parseFromString(commaSplit[4]);
            double houseLean = Double.parseDouble(commaSplit[5]);
            Grade grade = Grade.parseGrade(commaSplit[6]);
            String pollsterName = commaSplit[7];
            polls.add(new Poll(date, rawDemPercent, rawRepPercent, sampleSize, voterModel, houseLean, grade,
                    pollsterName));
        }
        reader.close();

        return polls.toArray(new Poll[1]);
    }

    /**
     * Read in all the information about all the districts from csv files.
     *
     * @param districtFile     The name of the file containing the non-poll, non-blairvoyance info about each district.
     * @param pollFile         The name of the file containing all the polls.
     * @param blairvoyanceFile The name of the file containing the blairvoyance output data/\.
     * @return A list of districts representing each district in the districtFile.
     * @throws IOException If one of the files is missing or improperly formatted.
     */
    public static District[] parseFromCSV(String districtFile, String pollFile, String blairvoyanceFile) throws IOException {
        //Represents the line currently being read. Defined out here to avoid needless garbage collection.
        String line;

        BufferedReader pollFileReader = new BufferedReader(new FileReader(pollFile));
        //Clear header line
        pollFileReader.readLine();
        //Associate district names to polls so they can be used later, when constructing district objects.
        Map<String, List<Poll>> nameToPollMap = new HashMap<>();
        while ((line = pollFileReader.readLine()) != null) {
            String[] commaSplit = line.split(",");
            //File must be formatted as follows:
            //district name,final date the poll was taken,dem percent,rep percent,sample size,which voter model was
            // used,pollster lean,pollster grade,pollster name
            String name = commaSplit[0].toUpperCase(); //Capitalize state postal code
            LocalDate date = LocalDate.parse(commaSplit[1], DateTimeFormatter.ofPattern("M/d/yyyy"));
            double rawDemPercent = Double.parseDouble(commaSplit[2]);
            double rawRepPercent = Double.parseDouble(commaSplit[3]);
            double sampleSize = Double.parseDouble(commaSplit[4]);
            Poll.VoterModel voterModel = Poll.VoterModel.parseFromString(commaSplit[5]);
            double houseLean = Double.parseDouble(commaSplit[6]);
            Grade grade = Grade.parseGrade(commaSplit[7]);
            String pollsterName = commaSplit[8];
            Poll poll = new Poll(date, rawDemPercent, rawRepPercent, sampleSize, voterModel, houseLean, grade,
                    pollsterName);

            if (nameToPollMap.containsKey(name)) {
                //Just add the poll to the district's list if it already exists
                nameToPollMap.get(name).add(poll);
            } else {
                //If it's the first poll for that district, make a new list with only that poll.
                List<Poll> pollList = new ArrayList<>();
                pollList.add(poll);
                nameToPollMap.put(name, pollList);
            }
        }
        pollFileReader.close();

        BufferedReader blairvoyanceFileReader = new BufferedReader(new FileReader(blairvoyanceFile));
        //Clear header line
        blairvoyanceFileReader.readLine();

        //Associate district names to Blairvoyance predictions so they can be used later when constructing district
        // objects.
        Map<String, Double> nameToBlairvoyanceMap = new HashMap<>();
        while ((line = blairvoyanceFileReader.readLine()) != null) {
            String[] commaSplit = line.split(",");
            //File must be formatted as follows:
            //district name,dem percent according to blairvoyance
            String name = commaSplit[0].toUpperCase(); //Capitalize state postal code
            nameToBlairvoyanceMap.put(name, Double.parseDouble(commaSplit[1]));
        }
        blairvoyanceFileReader.close();

        BufferedReader districtFileReader = new BufferedReader(new FileReader(districtFile));
        //Clear header line
        districtFileReader.readLine();
        List<District> toRet = new ArrayList<>();
        while ((line = districtFileReader.readLine()) != null) {
            String[] commaSplit = line.split(",");
            //File must be formatted as follows:
            //district name,rep incumbent (1 or 0),dem incumbent (1 or 0),Obama's 2012 margin,dem 2014 margin,
            // Hillary's 2016 margin,dem 2016 margin,elasticity,rep running (true or false),dem running (true or false)
            String name = commaSplit[0].toUpperCase(); //Capitalize state postal code
            boolean repIncumbent = Integer.parseInt(commaSplit[1]) == 1;
            boolean demIncumbent = Integer.parseInt(commaSplit[2]) == 1;
            double obama2012 = Double.parseDouble(commaSplit[3]);

            //Try to parse, if it's N/A an error will be thrown and we leave it as null.
            Double dem2014 = null;
            try {
                dem2014 = Double.parseDouble(commaSplit[4]);
            } catch (NumberFormatException ignored) {
            }

            double hillary2016 = Double.parseDouble(commaSplit[5]);

            //Try to parse, if it's N/A an error will be thrown and we leave it as null.
            Double dem2016 = null;
            try {
                dem2016 = Double.parseDouble(commaSplit[6]);
            } catch (NumberFormatException ignored) {
            }

            double elasticity = Double.parseDouble(commaSplit[7]);
            boolean repRunning = Boolean.parseBoolean(commaSplit[8]);
            boolean demRunning = Boolean.parseBoolean(commaSplit[9]);

            //Find all the polls for this district, or leave polls null if there are none.
            Poll[] polls = null;
            if (nameToPollMap.containsKey(name)) {
                polls = nameToPollMap.get(name).toArray(new Poll[1]);
            }

            //Find the Blairvoyance data for this district, or leave it null if there's none.
            Double blairvoyanceDemPercent = null;
            if (nameToBlairvoyanceMap.containsKey(name)) {
                blairvoyanceDemPercent = nameToBlairvoyanceMap.get(name);
            }

            toRet.add(new District(name, polls, repIncumbent, demIncumbent, obama2012, dem2014,
                    hillary2016, dem2016, elasticity, blairvoyanceDemPercent, repRunning, demRunning));
        }
        districtFileReader.close();

        return toRet.toArray(new District[435]);
    }

	public static Map<String, Integer> get2014Turnout(String file) throws IOException {
		String line;
		BufferedReader fileReader = new BufferedReader(new FileReader(file));
	    //Clear header
	    fileReader.readLine();
	    Map<String, Integer> districtToVoteMap = new HashMap<>();
	    while ((line = fileReader.readLine()) != null) {
	        String[] splitLine = line.split(",");
	        int demVote = Integer.parseInt(splitLine[1]);
	        int repVote = Integer.parseInt(splitLine[2]);
	        //Only count contested districts
	        if (demVote != 0 && repVote != 0) {
	            districtToVoteMap.put(splitLine[0].toUpperCase(), demVote + repVote);
	        }
	    }
	    fileReader.close();
	    
	    return districtToVoteMap;
	}
	
	public static Map<String, Integer> get2016Turnout(String file) throws IOException {
		String line;
		BufferedReader fileReader = new BufferedReader(new FileReader(file));
	    //Clear header
	    fileReader.readLine();
	    Map<String, Integer> districtToVoteMap = new HashMap<>();
	    while ((line = fileReader.readLine()) != null) {
	        String[] splitLine = line.split(",");
	        int votes = Integer.parseInt(splitLine[1]);
	        districtToVoteMap.put(splitLine[0].toUpperCase(), votes);
	    }
	    fileReader.close();
	    
	    return districtToVoteMap;
	}

}
