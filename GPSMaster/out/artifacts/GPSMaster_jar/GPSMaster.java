import java.io.*;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Gary on 24-Nov-15.
 */
public class GPSMaster {
    public static final double D2R = (3.141592653589793238/180);
    public static final int E_RADIUS = 6367137;
    private HashMap<Integer,GlobalPosition> positions;
    private HashMap<Integer, Split> splits;
    private double overallDistance;
    private double overallTime;
    private double averageSpeed;
    private double avgKMPace;
    private double avgMilePace;
    private String fileName;

    public GPSMaster(String fileName) {
        this.fileName = fileName;
        splits = new HashMap<>();
        positions = new HashMap<>();

        readData(fileName);
        totalDistanceAndTimes();
        averageSpeed();
        averageKMPace();
        averageMilePace();
        writeInfoToFile();
    }

    private void readData(String fileName) {
        int hashMapIndex = 0;
        BufferedReader fileReader;
        double currLat;
        double currLon;
        double currElev;
        String currTime;
        boolean needsClosingBrace = false;
        boolean needsOpeningBrace;

        try {
            fileReader = new BufferedReader(new FileReader(fileName));
            String line = fileReader.readLine().replaceAll("\\s", "");

            while(!(line.equalsIgnoreCase("<trkseg>"))) {
                line = fileReader.readLine().replaceAll("\\s", "");
            }

            while(!(line.equalsIgnoreCase("</trkseg>"))) {
                currLat = 0;
                currLon = 0;
                currElev = 0;
                currTime = "";
                needsOpeningBrace = true;

                while(!(line.equalsIgnoreCase("</trkpt>"))) {
                    if(line.contains("<trkpt")) {
                        needsOpeningBrace = false;
                        if(needsClosingBrace ==  true) {
                            CorruptDataException();
                        }
                        needsClosingBrace = true;
                        currLat = readDoubleAfterString(line, "lat=");
                        currLon = readDoubleAfterString(line, "lon=");
                    }
                    else if(line.contains("<ele>")) {
                        try {
                            currElev = Double.parseDouble(readStringAfterToken(line, "<ele>"));
                        } catch(Exception e) {
                            CorruptDataException();
                        }
                    }
                    else if(line.contains("<time>")) {
                        currTime = readStringAfterToken(line, "<time>");
                        if(currTime == null || currTime.length() == 0) {
                            CorruptDataException();
                        }
                        currTime = timeFromString(currTime);
                    }
                    line = fileReader.readLine().replaceAll("\\s", "");
                }
                needsClosingBrace = false;
                if(needsOpeningBrace) {
                    CorruptDataException();
                }
                needsOpeningBrace = true;

                if(currLat == -1 || currLon == -1 || currTime.equals("") || currElev == 0.0) {
                    CorruptDataException();
                }

                line = fileReader.readLine().replaceAll("\\s", "");
                positions.put(hashMapIndex++, new GlobalPosition(currLat, currLon, currElev, currTime));
            }

        } catch(Exception e ) {
            System.out.println("Unexpected error opening/reading file (check file format, must be .gpx)");
            System.exit(0);
        }
    }

    /**This method takes in two global positions as input and calculates the distance between them in meters.
     *
     * @param lat1  Latitude of Position 1
     * @param lat2  Latitude of Position 2
     * @param lon1  Longitude of Position 1
     * @param lon2  Longitude of Positon 2
     * @return  Distance between two global positions
     */
    private double haversine(double lat1, double lat2, double lon1, double lon2) {
        double distanceLat = (lat1 - lat2) * D2R;
        double distanceLon = (lon2 - lon1) * D2R;
        double a = Math.pow(Math.sin(distanceLat/2), 2) + Math.cos(lat1*D2R)*Math.cos(lat2*D2R)*Math.pow(Math.sin(distanceLon/2), 2);

        return E_RADIUS * 2*Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    }

    /**
     *
     * @param line line to search token in
     * @param token string to search for in line
     * @return double contained within double quotes in line
     */
    private double readDoubleAfterString(String line, String token) {
        if(line.contains(token)) {
            try {
                return Double.parseDouble(line.split(token)[1].split("\"")[1]);
            } catch(Exception e) {
                CorruptDataException();
            }
        }

       return -1;
    }

    /**
     *
     * @param line line to search token for
     * @param token string to search for in line
     * @return string following directly after token
     */
    private String readStringAfterToken(String line, String token) {
        if(line.contains(token)) {
            return line.split(token)[1].split("<")[0];
        }

        return null;
    }

    /**Method responsible for getting the splits over 1Km, 1 Mile, the elevations of each split. Stores this data in a Split object.
     *
     *
     */
    private void totalDistanceAndTimes() {
        double runningDistance = 0;
        double runningTime = 0;
        double runningSplitTimeKm = 0;
        double runningSplitDistanceKm = 0;
        double runningSplitTimeMile = 0;
        double runningSplitDistanceMile = 0;
        double distanceTemp;
        double timeTemp;
        double runningElevKm = 0;
        double runningElevMile = 0;
        double currElevChange;
        double endOfSplitTimeKm = 0;
        double endOfSplitTimeMile = 0;
        double endOfSplitDistanceKm = 0;
        double endOfSplitDistanceMile = 0;
        int splitIndex = 1;

        for(int i=0; i < positions.size()-1; i++) {
            distanceTemp = haversine(positions.get(i).getLatitude(), positions.get(i+1).getLatitude(), positions.get(i).getLongitude(), positions.get(i+1).getLongitude());
            runningSplitDistanceKm += distanceTemp;
            runningSplitDistanceMile += distanceTemp;
            runningDistance += distanceTemp;

            timeTemp = timeBetweenTwoPositions(positions.get(i), positions.get(i+1));
            runningSplitTimeKm += timeTemp;
            runningSplitTimeMile += timeTemp;
            runningTime += timeTemp;

            currElevChange = (positions.get(i+1).getElevation() - positions.get(i).getElevation());
            runningElevKm += currElevChange;
            runningElevMile += currElevChange;

            //For getting the average speed over the last 25 meters of a split (For readjusting inaccurate distance traveled)
            if(runningSplitDistanceKm > 975) {
                endOfSplitTimeKm += timeTemp;
                endOfSplitDistanceKm += distanceTemp;

                if(runningSplitDistanceKm > 1000) {
                    double extraTimeToCarry = adjustTimeTakenOverDistance(runningSplitDistanceKm - 1000, endOfSplitTimeKm, endOfSplitDistanceKm);
                    double realSplitTime = runningSplitTimeKm - extraTimeToCarry;

                    splits.put(splitIndex, new Split(realSplitTime, 1000, runningElevKm, false));
                    runningSplitTimeKm = extraTimeToCarry;
                    runningSplitDistanceKm = runningSplitDistanceKm - 1000;
                    runningElevKm = 0;
                    splitIndex++;
                }
            }

            //Done for the same as done above for the Km splits
            if(runningSplitDistanceMile > 1584.34) {
                endOfSplitTimeMile += timeTemp;
                endOfSplitDistanceMile += distanceTemp;

                if(runningSplitDistanceMile > 1609.34) {
                    double extraTimeToCarry = adjustTimeTakenOverDistance(runningSplitDistanceMile - 1609.34, endOfSplitTimeMile, endOfSplitDistanceMile);
                    double realSplitTime = runningSplitTimeMile - extraTimeToCarry;

                    splits.put(splitIndex, new Split(realSplitTime, 1609.34, runningElevMile, false));
                    runningSplitTimeMile = extraTimeToCarry;
                    runningSplitDistanceMile = runningSplitDistanceMile - 1609.34;
                    runningElevMile = 0;
                    splitIndex++;
                }
            }
        }

        splits.put(splitIndex++, new Split(runningSplitTimeKm + getProjectedTimeForUnfinishedSplit(runningSplitDistanceKm, 1000, runningSplitTimeKm), 1000, runningElevKm, true));
        splits.put(splitIndex++, new Split(runningSplitTimeMile + getProjectedTimeForUnfinishedSplit(runningSplitDistanceMile, 1609.34, runningSplitTimeMile), 1609.34, runningElevMile, true));
        overallDistance = runningDistance;
        overallTime = runningTime;
    }

    /**
     *
     * @param pos1 position 1
     * @param pos2 position 2
     * @return time difference in seconds between position 1 and 2
     */
    private double timeBetweenTwoPositions(GlobalPosition pos1, GlobalPosition pos2) {
        double timeAtPos1,timeAtPos2;

        timeAtPos1 = (pos1.getTime().getHours()*60*60) + (pos1.getTime().getMinutes()*60)+ pos1.getTime().getSeconds();
        timeAtPos2 = (pos2.getTime().getHours()*60*60) + (pos2.getTime().getMinutes()*60)+ pos2.getTime().getSeconds();
        return timeAtPos2 - timeAtPos1;
    }

    /**
     *
     * @param timeString String containing date and time
     * @return String in HH:MM:SS format
     */
    private String timeFromString(String timeString) {
        int hrs = Integer.parseInt(timeString.substring(11, 13));
        int mins = Integer.parseInt(timeString.substring(14, 16));
        float secs = Float.parseFloat(timeString.substring(17, 19));

        return hrs+":"+mins+":"+secs;
    }

    private void averageSpeed() {
        averageSpeed = (overallDistance/1000)/((overallTime/60)/60);
    }

    /**Assigns avgKmPace the average time it toke to do a kilometre. Ignores projected splits
     *
     */
    private void averageKMPace() {
        double runningAvg=0;
        int numSplits = 0;

        for(Split currSplit: splits.values()) {
            if(currSplit.getSplit() == 1000 && !(currSplit.isProjectedSplit())) {
                numSplits++;
                runningAvg += (currSplit.getTime()/60) / 1;
            }
        }
        avgKMPace = runningAvg/numSplits;
    }

    /**Assigns avgMilePace the average time it toke to do a mile. Ignores projected splits
     *
     */
    private void averageMilePace() {
        double runningAvg=0;
        int numSplits = 0;

        for(Split currSplit: splits.values()) {
            if(currSplit.getSplit() == 1609.34 && !(currSplit.isProjectedSplit())) {
                numSplits++;
                runningAvg += (currSplit.getTime()/60) / 1;
            }
        }
        avgMilePace = runningAvg/numSplits;
    }

    /**
     *AverageDistance hasn't been calculated at an early point in the program when it's required so we'll dynamically
     *get the current average speed for whatever distance we've traveled into this Km.
     *
     * @param distanceSoFar distance travelled
     * @param wantedDistance target split distance
     * @param timeSoFar time for split thusfar
     * @return Est. time for split
     */
    private double getProjectedTimeForUnfinishedSplit(double distanceSoFar, double wantedDistance, double timeSoFar) {
        double avgSpeed = (distanceSoFar/1000)/(timeSoFar/60/60);
        return (wantedDistance-distanceSoFar)/(((avgSpeed/60)/60)*1000);
    }

    //TODO: use average speed over last, say, 50 meters not the entire split! ::Done
    /**
     * Not fully possible to get time taken over exactly 1km or 1mile with data so this method gets the extra distance traveled
     * over the required distance and works how far you've traveled based on average speed
     * @param extraDistanceTraveled Extra distance traveled over split
     * @return Time to subtract from this split
     */
    private double adjustTimeTakenOverDistance(double extraDistanceTraveled, double time, double distance) {
        double avgSpeed = distance/time;
        return extraDistanceTraveled/(((avgSpeed/60)/60)*1000);
    }

    /**
     *
     * @param distance String representing a split, either Km or Mile
     * @return String in format MM:SS
     */
    private String getAveragePaceIn(String distance) {
        double decimalToSecs;
        String split;

        switch (distance) {
            case "Mile":
                decimalToSecs = avgMilePace;
                split="Mile";
                break;
            default:
                decimalToSecs= avgKMPace;
                split="Km";
                break;
        }

        return split(decimalToSecs, split);
    }

    /**
     *
     * @param time  time in seconds
     * @param splitType type Km or Mile
     * @return String in correct time format e.g(3:28 m/Km)
     */
    private String split(double time, String splitType) {
        int mins =0;
        while(time > 1) {
            time -= 1;
            mins += 1;
        }
        time *= 60;

        return mins+":"+((time < 10) ? ("0"+ (int)time) : (int)time);
    }

    /**Simply writes the data obtained from the rest of the program to a .CSV file
     *
     */
    private void writeInfoToFile() {
        String KmSplits = "";
        String MileSplits = "";
        Iterator it = splits.entrySet().iterator();
        int splitKmIndex = 1;
        int splitMileIndex = 1;
        FileWriter writer = null;
        try {
            writer = new FileWriter("SplitResults.csv");
            writer.append("File Name:"+","+getFileName()+"\n\n");
            writer.append("Split:,Avg Speed(km/h),Pace(m/Km),Elevation(m)");
            writer.append("\n");

            while(it.hasNext()) {
                HashMap.Entry<Integer, Split> entry = (HashMap.Entry<Integer, Split>)it.next();

                if(entry.getValue().getSplit() == 1000) {
                    KmSplits += ((splitKmIndex < 10) ? ("0" + splitKmIndex) : splitKmIndex)+ "," + (entry.getValue()).toString()+ "\n";
                    splitKmIndex++;
                }
                else if(entry.getValue().getSplit() == 1609.34) {
                    MileSplits += ((splitMileIndex < 10) ? ("0" + splitMileIndex) : splitMileIndex) + "," + (entry.getValue()).toString() +"\n";
                    splitMileIndex++;
                }

            }
            writer.append(KmSplits);
            writer.append("\n\n");
            writer.append("Split:,Avg Speed(km/h),Pace(m/M),Elevation(m)\n");
            writer.append(MileSplits);
            writer.append("\n\n");
            writer.append("Total Distance(m):,"+getOverallDistance()+"\n");
            writer.append("Time(secs):,"+getOverallTime()+"\n");
            writer.append("Avg Speed(km/h):," +getAverageSpeed()+"\n");
            writer.append("Avg Mile Pace(m/M):,"+getAveragePaceIn("Mile")+"\n");
            writer.append("Avg Km Pace(m/Km):,"+getAveragePaceIn("Km")+"\n");
            System.out.println("CSV file successfully created !!");

        }
        catch(Exception e) {
            System.out.println("Unexpected error writing to .csv file");
        }
        finally {
            try {
                writer.flush();
                writer.close();
            }catch(IOException e) {
                System.out.println("Unexpected error flushing/closing file writer");
            }
        }
    }

    private void CorruptDataException() {
        System.out.println("Unexpected Error: data is corrupt");
        System.exit(0);
    }

    public String getOverallDistance() {
        return String.format("%.2f",overallDistance);
    }

    public double getOverallTime() {
        return overallTime;
    }

    public String getAverageSpeed() {
        return String.format("%.2f", averageSpeed);
    }

    public String getFileName() {
        return this.fileName;
    }

    public static void main(String[] args) {
        if(args.length > 0 ) {
           new GPSMaster(args[0]);
        }
        else {
            System.out.println("Error: No command line arugments given");
            System.exit(0);
        }

    }
}