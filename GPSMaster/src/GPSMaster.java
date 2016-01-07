import jdk.nashorn.internal.objects.Global;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Gary on 24-Nov-15.
 */
public class GPSMaster {
    public static final double D2R = (3.141592653589793238/180);
    public static final int E_RADIUS = 6367137;
    private ArrayList<GlobalPosition> positions;
    private ArrayList<Double> KMSplits;
    private ArrayList<Double> MileSplits;
    private HashMap<Integer, Split> splits;
    private double overallDistance;
    private double overallTime;
    private double averageSpeed;
    private double avgKMPace;
    private double avgMilePace;

    public GPSMaster() {
        splits = new HashMap<>();
        positions = new ArrayList<>();
        KMSplits = new ArrayList<>();
        MileSplits = new ArrayList<>();

        //readData("./inputFiles/1 Mile Sprint.gpx");
        readData("./inputFiles/16Km Dunshaughlin.gpx");
        totalDistanceAndTimes();
        averageSpeed();
        averageKMPace();
        averageMilePace();
        printSplits();
    }

    private void readData(String fileName) {
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
                positions.add(new GlobalPosition(currLat, currLon, currElev, currTime));
            }

        } catch(IOException e ) {
            e.printStackTrace();
        }
    }

    private double haversine(double lat1, double lat2, double lon1, double lon2) {
        double distanceLat = (lat1 - lat2) * D2R;
        double distanceLon = (lon2 - lon1) * D2R;
        double a = Math.pow(Math.sin(distanceLat/2), 2) + Math.cos(lat1*D2R)*Math.cos(lat2*D2R)*Math.pow(Math.sin(distanceLon/2), 2);

        return E_RADIUS * 2*Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    }

    //Grabs double value associated with lat/lon
    private double readDoubleAfterString(String line, String token) {
        if(line.contains(token)) {
            return Double.parseDouble(line.split(token)[1].split("\"")[1]);
        }
        return -1;
    }

    private String readStringAfterToken(String line, String token) {
        if(line.contains(token)) {
            return line.split(token)[1].split("<")[0];
        }
        return null;
    }

    //TODO: get splits for Mile's/Km's here
    private void totalDistanceAndTimes() {
        double runningDistance = 0;
        double runningTime = 0;
        double runningSplitTimeKm = 0;
        double runningSplitDistanceKm = 0;
        double runningSplitTimeMile = 0;
        double runningSplitDistanceMile = 0;
        double distanceTemp = 0;
        double timeTemp = 0;
        double runningElevKm = 0;
        double runningElevMile = 0;
        double currElevChange = 0;
        double overallElev = 0;
        int splitIndex = 1;

        //We're going to be resetting the split after each km/mile so we add the split distance to overall distance, not the other way round
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
            //System.out.println(currElev);
            runningElevKm += currElevChange;
            runningElevMile += currElevChange;

            if(runningSplitDistanceKm > 1000) {
                double extraTimeToCarry = adjustTimeTakenOverDistance(runningSplitDistanceKm - 1000, runningSplitTimeKm, 1000);
                double realSplitTime = runningSplitTimeKm - extraTimeToCarry;

                splits.put(splitIndex, new Split(realSplitTime, 1000, runningElevKm, false));
                runningSplitTimeKm = extraTimeToCarry;
                runningSplitDistanceKm = runningSplitDistanceKm - 1000;
                runningElevKm = 0;
                splitIndex++;
            }
            else if(runningSplitDistanceMile > 1609.34) {
                double extraTimeToCarry = adjustTimeTakenOverDistance(runningSplitDistanceMile - 1609.34, runningSplitTimeMile, 1609.34);
                double realSplitTime = runningSplitTimeMile - extraTimeToCarry;

                splits.put(splitIndex, new Split(realSplitTime, 1609.34, runningElevMile, false));
                runningSplitTimeMile = extraTimeToCarry;
                runningSplitDistanceMile = runningSplitDistanceMile - 1609.34;
                runningElevMile = 0;
                splitIndex++;
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
        ArrayList<Double> splitTimes = getTimeSplitsFor(1000);
        KMSplits = splitTimes;

        for(double split: splitTimes) {
            runningAvg += (split/60) / 1;
        }
        avgKMPace = runningAvg/splitTimes.size();
    }

    private void averageMilePace() {
        double runningAvg=0;
        ArrayList<Double> splitTimes = getTimeSplitsFor(1609.34);
        MileSplits = splitTimes;

        for(double split: splitTimes) {
            runningAvg += (split/60) / 1;
        }
         avgMilePace = runningAvg/splitTimes.size();
    }
    //TODO: make algorithm to subtract time for extra distance traveled over split::DONE
    private ArrayList<Double> getTimeSplitsFor(double distance) {
        ArrayList<Double> timeTaken = new ArrayList<>();
        double distanceUndertaken = 0;
        double splitTime=0;

        for(int i=0; i < positions.size()-1; i++) {
            distanceUndertaken += haversine(positions.get(i).getLatitude(), positions.get(i+1).getLatitude(), positions.get(i).getLongitude(), positions.get(i+1).getLongitude());
            splitTime += timeBetweenTwoPositions(positions.get(i), positions.get(i+1));

            if(distanceUndertaken > distance) {
                //Potentially broken
                splitTime -= adjustTimeTakenOverDistance(distanceUndertaken - distance, splitTime, distance);
                timeTaken.add(splitTime);
                distanceUndertaken = 0;
                splitTime = 0;
            }
        }
        //TODO: get average pace for distance left over::DONE
        timeTaken.add(splitTime + getProjectedTimeForUnfinishedSplit(distanceUndertaken, distance));
        return timeTaken;
    }

    private double getProjectedTimeForUnfinishedSplit(double distanceSoFar, double wantedDistance) {
        return (wantedDistance-distanceSoFar)/(((averageSpeed/60)/60)*1000);
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

    public String getAveragePaceIn(String distance) {
        double decimalToSecs;
        String split ="";

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