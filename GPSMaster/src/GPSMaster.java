import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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

    public GPSMaster() {
        splits = new HashMap<>();
        positions = new HashMap<>();

        //readData("./inputFiles/1 Mile Sprint.gpx");
        readData("./inputFiles/16Km Dunshaughlin.gpx");
        totalDistanceAndTimes();
        averageSpeed();
        averageKMPace();
        averageMilePace();
        printSplits();
    }

    private void readData(String fileName) {
        int hashMapIndex = 0;
        BufferedReader fileReader;
        double currLat = 0;
        double currLon = 0;
        double currElev = 0;
        String currTime = "";

        try {
            fileReader = new BufferedReader(new FileReader(fileName));
            String line = fileReader.readLine().replaceAll("\\s", "");

            while(!(line.equalsIgnoreCase("<trkseg>"))) {
                line = fileReader.readLine().replaceAll("\\s", "");
            }

            while(!(line.equalsIgnoreCase("</trkseg>"))) {
                while(!(line.equalsIgnoreCase("</trkpt>"))) {
                    if(line.contains("<trkpt")) {
                        currLat = readDoubleAfterString(line, "lat=");
                        currLon = readDoubleAfterString(line, "lon=");
                    }
                    else if(line.contains("<ele>")) {
                        currElev = Double.parseDouble(readStringAfterToken(line, "<ele>"));
                    }
                    else if(line.contains("<time>")) {
                        currTime = readStringAfterToken(line, "<time>");
                        currTime = timeFromString(currTime);
                    }
                    line = fileReader.readLine().replaceAll("\\s", "");
                }
                line = fileReader.readLine().replaceAll("\\s", "");
                positions.put(hashMapIndex++, new GlobalPosition(currLat, currLon, currElev, currTime));
            }

        } catch(IOException e ) {
            e.printStackTrace();
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

    //Grabs double value associated with lat/lon
    //TODO: Change to regular expressions to avoid potential ArrayOutOfBoundsExceptions
    private double readDoubleAfterString(String line, String token) {
        if(line.contains(token)) {
            return Double.parseDouble(line.split(token)[1].split("\"")[1]);
        }
        return -1;
    }

    //TODO: Change to regular expressions to avoid potential ArrayOutOfBoundsExceptions
    private String readStringAfterToken(String line, String token) {
        if(line.contains(token)) {
            return line.split(token)[1].split("<")[0];
        }
        return null;
    }

    //TODO: get splits for Mile's/Km's here :: DONE

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
            if ((i + 1) == positions.size() - 1) {
                break;
            }
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

            if(runningSplitDistanceKm > 975) {
                endOfSplitTimeKm += timeTemp;
                endOfSplitDistanceKm += distanceTemp;

                if(runningSplitDistanceKm > 1000) {
                    double extraTimeToCarry = adjustTimeTakenOverDistance(runningSplitDistanceKm - 1000, endOfSplitTimeKm, endOfSplitDistanceKm);
                    double realSplitTime = runningSplitTimeKm - extraTimeToCarry;

                    splits.put(splitIndex, new Split(realSplitTime, 1000, runningElevKm, false));
                    runningSplitTimeKm = 0;
                    runningSplitDistanceKm = 0.0;
                    runningElevKm = 0;
                    splitIndex++;
                }
            }
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

    private double timeBetweenTwoPositions(GlobalPosition pos1, GlobalPosition pos2) {
        double timeAtPos1,timeAtPos2;
        timeAtPos1 = (pos1.getTime().getHours()*60*60) + (pos1.getTime().getMinutes()*60)+ pos1.getTime().getSeconds();
        timeAtPos2 = (pos2.getTime().getHours()*60*60) + (pos2.getTime().getMinutes()*60)+ pos2.getTime().getSeconds();
        return timeAtPos2 - timeAtPos1;
    }

    private String timeFromString(String timeString) {               //TODO: change to regular expressions
        int hrs = Integer.parseInt(timeString.substring(11, 13));
        int mins = Integer.parseInt(timeString.substring(14, 16));
        float secs = Float.parseFloat(timeString.substring(17, 21));

        return hrs+":"+mins+":"+secs;
    }

    private void averageSpeed() {
        averageSpeed = (overallDistance/1000)/((overallTime/60)/60);
    }

    private void averageKMPace() {
        double runningAvg=0;
        int numSplits = 0;

        for(Split currSplit: splits.values()) {
            if(currSplit.getSplit() == 1000) {
                numSplits++;
                runningAvg += (currSplit.getTime()/60) / 1;
            }
        }
        avgKMPace = runningAvg/numSplits;
    }

    private void averageMilePace() {
        double runningAvg=0;
        int numSplits = 0;

        for(Split currSplit: splits.values()) {
            if(currSplit.getSplit() == 1609.34) {
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

    //TODO: use average speed over last, say, 50 meters not the entire split!
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

        return mins+":"+(int)time+" m/"+splitType;
    }

    private void printSplits() {
        String KmSplits = "";
        String MileSplits = "";
        Iterator it = splits.entrySet().iterator();
        int splitKmIndex = 1;
        int splitMileIndex = 1;

        System.out.println("--------------------------------------------------------------");
        System.out.println("-- Split  |  Avg.Speed(km/h)  |  Pace(m/Km)  | Elevation(m) --");
        System.out.println("----------+-------------------+--------------+----------------");

        while(it.hasNext()) {
            HashMap.Entry<Integer, Split> entry = (HashMap.Entry<Integer, Split>)it.next();

            if(entry.getValue().getSplit() == 1000) {
                KmSplits += ("--  " + ((splitKmIndex < 10) ? ("0" + splitKmIndex) : splitKmIndex) + (entry.getValue()).toString())+ "\n";
                splitKmIndex++;
            }
            else if(entry.getValue().getSplit() == 1609.34) {
                MileSplits += ("--  " + ((splitMileIndex < 10) ? ("0" + splitMileIndex) : splitMileIndex) + (entry.getValue()).toString())+"\n";
                splitMileIndex++;
            }

        }
        System.out.println(KmSplits+"\n\n");
        System.out.println("--------------------------------------------------------------");
        System.out.println("-- Split  |  Avg.Speed(km/h)  |   Pace(m/M)  | Elevation(m) --");
        System.out.println("----------+-------------------+--------------+----------------");
        System.out.println(MileSplits);
    }


    public double getOverallDistance() {
        return overallDistance;
    }

    public double getOverallTime() {
        return overallTime;
    }

    public double getAverageSpeed() {
        return averageSpeed;
    }

    public static void main(String[] args) {
        GPSMaster myGps = new GPSMaster();
        System.out.println("Distance: "+myGps.getOverallDistance()+" metres");
        System.out.println("Time: "+myGps.getOverallTime()+" secs");
        System.out.println("Avg.Speed: "+myGps.getAverageSpeed()+" km/h");
        System.out.println("Avg Mile Pace: "+ myGps.getAveragePaceIn("Mile"));
        System.out.println("Avg Km Pace: "+ myGps.getAveragePaceIn("Km"));
    }
}