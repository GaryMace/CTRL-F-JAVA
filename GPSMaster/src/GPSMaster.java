import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**{'-'} :: Hi there!
 *
 * Last Modified: 1st Aug 2016
 * Created by Gary on 24-Nov-15.
 */
public final class GPSMaster {
        public static final double D2R = (3.141592653589793238 / 180);
        public static final double GPSMASTER_MILE = 1609.34;
        public static final int GPSMASTER_KILOMETER = 1000;
        public static final int GPSMASTER_ENHANCEMENT_DISTANCE = 25;
        public static final int E_RADIUS = 6367137;

        private static final int GPSMASTER_VALID_DATA_ENTRY = 0;
        private static final int GPSMASTER_ERROR_INVALID_ELEVATION_ENTRY = 1;
        private static final int GPSMASTER_ERROR_INVALID_TIME_ENTRY = 2;
        private static final boolean GPSMASTER_PROJECTED_SPLIT = true;

        private static ArrayList<GlobalPosition> positions = new ArrayList<>();
        private static HashMap<Integer, Split> splits = new HashMap<>();
        private static ArrayList<String> rawData = new ArrayList<>();

        private static String fileName;
        private static double overallDistance;
        private static double overallTime;
        private static double averageSpeed;
        private static double avgKMPace;
        private static double avgMilePace;

        private GPSMaster() {
        }

        public static void readFile(String fileName) {
                GPSMaster.fileName = fileName;
                try {
                        rawData = (ArrayList<String>) Files.readAllLines(Paths.get("./inputFiles/"+fileName));
                } catch(IOException e) { e.printStackTrace(); }
        }

        public static void getGPSPositions() {
                int errCode;
                if (rawData.isEmpty()) {
                        //Do something
                } else {
                        skipToStartOfData();    //TODO: check if this empties the file
                        for (int i = 0; i < rawData.size(); i++) {
                                String line = getRawDataAtLine(i); //Remove all whitespace from line
                                if (isGPSDataEntry(line)) {
                                        errCode = extractGPSEntry(i);   //TODO: handle the error code
                                }
                        }
                }
        }

        /**
         * Method uniformly runs through the GPX data and grabs information for each split based on the split distance
         * parameter passed. Needs to be at least 25..
         * @param splitDistance Size of each split
         */
        public static void getSplitsFor(double splitDistance) {
                double distanceTraveledSoFarInSplit =0;
                double timeTakenForSplit =0;
                double elevTrackingForSplit =0;
                GlobalPosition currPosition, nextPosition;
                int splitIndex = 0;

                for (int i=0; i < positions.size()-1; i++) {
                        currPosition = positions.get(i);
                        nextPosition = positions.get(i+1);

                        distanceTraveledSoFarInSplit += haversine(currPosition, nextPosition);
                        timeTakenForSplit += timeBetweenTwoPositions(currPosition, nextPosition);;
                        elevTrackingForSplit += elevationBetweenTwoPositions(currPosition, nextPosition);
                        if (distanceTraveledSoFarInSplit > splitDistance) {
                                double splitEnhancementSpeed = getEnhancementSpeedForEndOfSplitAt(i);
                                double redundantExtraTimeForSplit = adjustTimeTakenOverDistance(splitDistance, splitDistance - distanceTraveledSoFarInSplit, splitEnhancementSpeed);
                                double timeForSplit = timeTakenForSplit - redundantExtraTimeForSplit;

                                splits.put(splitIndex++, new Split(timeForSplit, splitDistance, elevTrackingForSplit, !GPSMASTER_PROJECTED_SPLIT));
                                distanceTraveledSoFarInSplit = splitDistance - distanceTraveledSoFarInSplit;
                                timeTakenForSplit = redundantExtraTimeForSplit;
                                elevTrackingForSplit = 0;
                        }
                }
                double projectedTime = timeTakenForSplit + getProjectedTimeForUnfinishedSplit(distanceTraveledSoFarInSplit, splitDistance, timeTakenForSplit);
                splits.put(splitIndex, new Split(projectedTime, splitDistance, elevTrackingForSplit, GPSMASTER_PROJECTED_SPLIT));
        }

        /**
         * This method takes in two global positions as input and calculates the distance between them in meters.
         *
         * @param pos1 GPS position on earth
         * @param pos2 next GPS position on earth
         * @return Distance between two global positions
         */
        private static double haversine(GlobalPosition pos1, GlobalPosition pos2) {
                double lat1 = pos1.getLatitude(), lon1 = pos1.getLongitude();
                double lat2 = pos2.getLatitude(), lon2 = pos2.getLongitude();
                double distanceLat = (lat1 - lat2) * D2R;
                double distanceLon = (lon2 - lon1) * D2R;
                double a = Math.pow(Math.sin(distanceLat / 2), 2) + Math.cos(lat1 * D2R) * Math.cos(lat2 * D2R) * Math.pow(Math.sin(distanceLon / 2), 2);

                return E_RADIUS * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        }

        /**
         * @param line  line to search token in
         * @param token string to search for in line
         * @return double contained within double quotes in line
         */
        private static double readDoubleAfterString(String line, String token) {
                Pattern toFind = Pattern.compile(token+"\"((-?)\\d+(\\.\\d+))\"");
                Matcher m = toFind.matcher(line);

                if (m.find()) {
                        return Double.parseDouble(m.group(1));
                }
                return -1;
        }

        /**
         * @param line  line to search token for
         * @param pattern string to search for in line
         * @return string following directly after token
         */
        private static String readStringAfterToken(String line, String pattern) {
                Pattern toFind = Pattern.compile(pattern);
                Matcher m = toFind.matcher(line);

                if (m.find()) {
                        return m.group(1);
                }
                return null;
        }

        /**
         * @param pos1 position 1
         * @param pos2 position 2
         * @return time difference in seconds between position 1 and 2
         */
        private static double timeBetweenTwoPositions(GlobalPosition pos1, GlobalPosition pos2) {
                double timeAtPos1, timeAtPos2;
                //java time date implementations
                timeAtPos1 = (pos1.getTime().getHours() * 60 * 60) + (pos1.getTime().getMinutes() * 60) + pos1.getTime().getSeconds();
                timeAtPos2 = (pos2.getTime().getHours() * 60 * 60) + (pos2.getTime().getMinutes() * 60) + pos2.getTime().getSeconds();
                return timeAtPos2 - timeAtPos1;
        }

        /**
         * @param timeString String containing date and time
         * @return String in HH:MM:SS format
         */
        private static String timeFromString(String timeString) {
                Pattern toFind = Pattern.compile("\\d{1,2}(:)\\d{1,2}(:)\\d+(\\.\\d+)");
                Matcher m = toFind.matcher(timeString);

                if (m.find()) {
                        return m.group();
                }
                return null;
        }

        public static void averageSpeed() {
                averageSpeed = (overallDistance / 1000) / ((overallTime / 60) / 60);
        }

        /**
         * Assigns avgKmPace the average time it toke to do a kilometre. Ignores projected splits
         */
        public static void averageKMPace() {
                double runningAvg = 0;
                int numSplits = 0;

                for (Split currSplit : splits.values()) {
                        if (currSplit.getSplit() == GPSMASTER_KILOMETER && !(currSplit.isProjectedSplit())) {
                                numSplits++;
                                runningAvg += (currSplit.getTime() / 60) / 1;
                        }
                }
                avgKMPace = runningAvg / numSplits;
        }

        /**
         * Assigns avgMilePace the average time it toke to do a mile. Ignores projected splits
         */
        public static void averageMilePace() {
                double runningAvg = 0;
                int numSplits = 0;

                for (Split currSplit : splits.values()) {
                        if (currSplit.getSplit() == GPSMASTER_MILE && !(currSplit.isProjectedSplit())) {
                                numSplits++;
                                runningAvg += (currSplit.getTime() / 60) / 1;
                        }
                }
                avgMilePace = runningAvg / numSplits;
        }

        /**
         * AverageDistance hasn't been calculated at an early point in the program when it's required so we'll dynamically
         * get the current average speed for whatever distance we've traveled into this Km.
         *
         * @param distanceSoFar  distance travelled
         * @param wantedDistance target split distance
         * @param timeSoFar      time for split thusfar
         * @return Est. time for split
         */
        private static double getProjectedTimeForUnfinishedSplit(double distanceSoFar, double wantedDistance, double timeSoFar) {
                double avgSpeed = (distanceSoFar / wantedDistance) / (timeSoFar / 60 / 60);
                return (wantedDistance - distanceSoFar) / (((avgSpeed / 60) / 60) * wantedDistance);
        }

        //TODO: use average speed over last, say, 50 meters not the entire split! ::Done

        /**
         * Not fully possible to get time taken over exactly 1km or 1mile with data so this method gets the extra distance traveled
         * over the required distance and works how far you've traveled based on average speed
         *
         * @param extraDistanceTraveled Extra distance traveled over split
         * @return Time to subtract from this split
         */
        private static double adjustTimeTakenOverDistance(double distanceWanted, double extraDistanceTraveled, double speed) {
                return extraDistanceTraveled / (((speed / 60) / 60) * distanceWanted); //distanceWanted was 1000
        }

        /**
         * @param distance String representing a split, either Km or Mile
         * @return String in format MM:SS
         */
        private static String getAveragePaceIn(String distance) {
                double decimalToSecs;
                String split;

                switch (distance) {//uncommon to use switch in this case
                        case "Mile":
                                decimalToSecs = avgMilePace;
                                split = "Mile";
                                break;
                        default:
                                decimalToSecs = avgKMPace;
                                split = "Km";
                                break;
                }

                return split(decimalToSecs, split);
        }

        /**
         * @param time      time in seconds
         * @param splitType type Km or Mile
         * @return String in correct time format e.g(3:28 m/Km)
         */
        private static String split(double time, String splitType) {
                int mins = 0;
                while (time > 1) {
                        time -= 1;
                        mins += 1;
                }
                time *= 60;

                return mins + ":" + ((time < 10) ? ("0" + (int) time) : (int) time);
        }

        /**
         * Simply writes the data obtained from the rest of the program to a .CSV file
         */
        public static void writeInfoToFile() {
                String KmSplits = "";
                String MileSplits = "";
                Iterator it = splits.entrySet().iterator();
                int splitKmIndex = 1;
                int splitMileIndex = 1;
                FileWriter writer = null;
                //filewriter are closable
                try {
                        writer = new FileWriter("SplitResults.csv");
                        writer.append("File Name:" + "," + fileName + "\n\n");
                        writer.append("Split:,Avg Speed(km/h),Pace(m/Km),Elevation(m)");
                        writer.append("\n");

                        while (it.hasNext()) {
                                HashMap.Entry<Integer, Split> entry = (HashMap.Entry<Integer, Split>) it.next();

                                if (entry.getValue().getSplit() == GPSMASTER_KILOMETER) {
                                        KmSplits += ((splitKmIndex < 10) ? ("0" + splitKmIndex) : splitKmIndex) + "," + (entry.getValue()).toString() + "\n";
                                        splitKmIndex++;
                                } else if (entry.getValue().getSplit() == GPSMASTER_MILE) {
                                        MileSplits += ((splitMileIndex < 10) ? ("0" + splitMileIndex) : splitMileIndex) + "," + (entry.getValue()).toString() + "\n";
                                        splitMileIndex++;
                                }

                        }
                        writer.append(KmSplits);
                        writer.append("\n\n");
                        writer.append("Split:,Avg Speed(km/h),Pace(m/M),Elevation(m)\n");
                        writer.append(MileSplits);
                        writer.append("\n\n");
                        writer.append("Total Distance(m):," + getOverallDistance() + "\n");
                        writer.append("Time(secs):," + getOverallTime() + "\n");
                        writer.append("Avg Speed(km/h):," + getAverageSpeed() + "\n");
                        writer.append("Avg Mile Pace(m/M):," + getAveragePaceIn("Mile") + "\n");
                        writer.append("Avg Km Pace(m/Km):," + getAveragePaceIn("Km") + "\n");
                        System.out.println("CSV file successfully created !!");

                } catch (Exception e) {
                        System.out.println("Unexpected error writing to .csv file");
                } finally {
                        try {
                                writer.flush();
                                writer.close();
                        } catch (IOException e) {
                                System.out.println("Unexpected error flushing/closing file writer");
                        }
                }
        }

        private static void CorruptDataException() {
                System.out.println("Unexpected Error: data is corrupt");
                System.exit(0);
        }

        public static String getOverallDistance() {
                return String.format("%.2f", overallDistance);
        }

        public static double getOverallTime() {
                return overallTime;
        }

        public static String getAverageSpeed() {
                return String.format("%.2f", averageSpeed);
        }

        private static String getRawDataAtLine(int index) {
                try {
                        return rawData.get(index).replaceAll("\\s", "");
                } catch(IndexOutOfBoundsException e) {
                        e.printStackTrace();
                }
                return "";
        }

        private static double elevationBetweenTwoPositions(GlobalPosition pos1, GlobalPosition pos2) {
                return pos2.getElevation() - pos1.getElevation();
        }

        /**
         * This method returns the avg. speed for the last 25ish meters of a split. This value will be used later to adjust
         * the time taken for a split since we cant get perfect splits of the data for a distance.
         * @param index Index into the GPX data
         * @return Avg. Speed for last 25ish meters of split
         */
        private static double getEnhancementSpeedForEndOfSplitAt(int index) {
                double distanceTracking =0, timeTracking =0;
                do {
                        GlobalPosition currPosition = positions.get(index), previousPosition = positions.get(index - 1);
                        distanceTracking += haversine(previousPosition, currPosition);
                        timeTracking += timeBetweenTwoPositions(previousPosition, currPosition);
                        index --;
                } while (distanceTracking < GPSMASTER_ENHANCEMENT_DISTANCE);
                return distanceTracking / timeTracking;
        }

        private static int extractGPSEntry(int currRawDataIndex) {
                String line = getRawDataAtLine(currRawDataIndex++);
                int errCode = GPSMASTER_VALID_DATA_ENTRY;
                double lat, lon, elevation;
                String time;

                lon = readDoubleAfterString(line, "lon=");
                lat = readDoubleAfterString(line, "lat=");

                line = getRawDataAtLine(currRawDataIndex++);
                if (validElevationEntry(line)) {
                        elevation = Double.parseDouble(readStringAfterToken(line, "<ele>((-?)\\d+(\\.\\d+))</ele>")); //No IntelliJ, it cant be null
                } else {
                        return GPSMASTER_ERROR_INVALID_ELEVATION_ENTRY;
                }

                line = getRawDataAtLine(currRawDataIndex);
                if (validTimeEntry(line)) {
                        String pattern = "<time>(\\d{1,4}(-)\\d{1,2}(-)\\d{1,2}T\\d{1,2}(:)\\d{1,2}(:)\\d+(\\.\\d+))Z</time>";
                        time = timeFromString(readStringAfterToken(line, pattern));
                } else {
                        return GPSMASTER_ERROR_INVALID_TIME_ENTRY;
                }
                positions.add(new GlobalPosition(lat, lon, elevation, time));
                return errCode;
        }

        private static boolean validTimeEntry(String line) {
                String dateRegex = "\\d{1,4}(-)\\d{1,2}(-)\\d{1,2}";
                String timeRegex = "\\d{1,2}(:)\\d{1,2}(:)\\d+(\\.\\d+)";

                return line.matches("(<time>)"+dateRegex+"T"+timeRegex+"(Z</time>)");
        }

        private static boolean validElevationEntry(String line) {
                return line.matches("(<ele>)\\d+(\\.\\d+)(</ele>)");
        }
        
        private static boolean isGPSDataEntry(String line) {
                return line.matches("(<trkptlon=\")(-?)\\d+(\\.\\d+)(\"lat=\")(-?)\\d+(\\.\\d+)\">");
        }

        private static void skipToStartOfData() {
                while (!getRawDataAtLine(0).matches("<trkseg>")) {
                        rawData.remove(0);
                }
                rawData.remove(0); //Removes the <trkseg> line
        }
}