import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**{'-'} :: Hi there!
 *
 * Last Modified: 1st Aug 2016
 * Created by Gary on 24-Nov-15.
 */
public final class GPSMaster {
        public static final double D2R = (3.141592653589793238 / 180);
        public static final double GPSMASTER_MILE = 1609.34;
        public static final int GPSMASTER_KILOMETER = 1000;
        public static final int E_RADIUS = 6367137;

        private static final int GPSMASTER_VALID_DATA_ENTRY = 0;
        private static final int GPSMASTER_ERROR_INVALID_ELEVATION_ENTRY = 1;
        private static final int GPSMASTER_ERROR_INVALID_TIME_ENTRY = 2;

        private static HashMap<Integer, GlobalPosition> positions = new HashMap<>();
        private static HashMap<Integer, Split> splits = new HashMap<>();
        private static ArrayList<String> rawData = new ArrayList<>();

        private static String fileName;
        private static double overallDistance;
        private static double overallTime;
        private static double averageSpeed;
        private static double avgKMPace;
        private static double avgMilePace;
        private static int GPSpositionIndex;

        private GPSMaster() {
        }

        public static void readFile(String fileName) {
                GPSMaster.fileName = fileName;
                GPSpositionIndex = 0;
                try {
                        rawData = (ArrayList<String>) Files.readAllLines(Paths.get("./inputFiles/"+fileName));
                } catch(IOException e) { e.printStackTrace(); }
        }

        public static void getGPSPositions() {
                int errCode;
                if (rawData.isEmpty()) {
                        //Do something
                } else {
                        skipToStartOfData();
                        //TODO: check if the above empties the file

                        for (int i = 0; i < rawData.size(); i++) {
                                String line = getRawDataAtLine(i); //Remove all whitespace from line
                                if (isGPSDataEntry(line)) {
                                        errCode = extractGPSEntry(i);
                                        //TODO: handle the error code
                                }
                        }
                }
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
                        elevation = Double.parseDouble(readStringAfterToken(line, "<ele>"));
                } else {
                        return GPSMASTER_ERROR_INVALID_ELEVATION_ENTRY;
                }

                line = getRawDataAtLine(currRawDataIndex);
                if (validTimeEntry(line)) {
                        time = timeFromString(readStringAfterToken(line, "<time>"));
                } else {
                        return GPSMASTER_ERROR_INVALID_TIME_ENTRY;
                }
                positions.put(GPSpositionIndex++, new GlobalPosition(lat, lon, elevation, time));
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

        private static boolean endOfGPSEntry(String line) {
                return line.matches("</trkpt>");
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

        public static void readData(String fileName) {
                int hashMapIndex = 0;
                BufferedReader fileReader;
                double currLat;
                double currLon;
                double currElev;
                String currTime;
                boolean needsClosingBrace = false;
                boolean needsOpeningBrace;
                GPSMaster.fileName = fileName;

                try {
                        fileReader = new BufferedReader(new FileReader(fileName));
                        String line = fileReader.readLine().replaceAll("\\s", "");

                        while (!(line.equalsIgnoreCase("<trkseg>"))) {
                                line = fileReader.readLine().replaceAll("\\s", "");
                        }

                        while (!(line.equalsIgnoreCase("</trkseg>"))) {
                                currLat = 0;
                                currLon = 0;
                                currElev = 0;
                                currTime = "";//consider making an object for this
                                needsOpeningBrace = true;

                                while (!(line.equalsIgnoreCase("</trkpt>"))) {
                                        if (line.contains("<trkpt")) {
                                                needsOpeningBrace = false;
                                                if (needsClosingBrace == true) {
                                                        CorruptDataException();
                                                }
                                                needsClosingBrace = true;
                                                currLat = readDoubleAfterString(line, "lat=");
                                                currLon = readDoubleAfterString(line, "lon=");
                                        } else if (line.contains("<ele>")) {
                                                try {
                                                        currElev = Double.parseDouble(readStringAfterToken(line, "<ele>"));
                                                } catch (Exception e) {  //handle this
                                                        CorruptDataException();
                                                }
                                        } else if (line.contains("<time>")) {
                                                currTime = readStringAfterToken(line, "<time>");
                                                if (currTime == null || currTime.length() == 0) {
                                                        CorruptDataException();
                                                }
                                                currTime = timeFromString(currTime);
                                        }
                                        line = fileReader.readLine().replaceAll("\\s", "");
                                }
                                needsClosingBrace = false;
                                if (needsOpeningBrace) {
                                        CorruptDataException();
                                }
                                needsOpeningBrace = true;

                                if (currLat == -1 || currLon == -1 || currTime.equals("") || currElev == 0.0) {
                                        CorruptDataException();
                                }

                                line = fileReader.readLine().replaceAll("\\s", "");
                                positions.put(hashMapIndex++, new GlobalPosition(currLat, currLon, currElev, currTime));
                        }

                } catch (Exception e) {
                        System.out.println("Unexpected error opening/reading file (check file format, must be .gpx)");
                        System.exit(0);
                }
        }

        /**
         * This method takes in two global positions as input and calculates the distance between them in meters.
         *
         * @param lat1 Latitude of Position 1
         * @param lat2 Latitude of Position 2
         * @param lon1 Longitude of Position 1
         * @param lon2 Longitude of Positon 2
         * @return Distance between two global positions
         */
        private static double haversine(double lat1, double lat2, double lon1, double lon2) {
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
                if (line.contains(token)) {
                        try {
                                return Double.parseDouble(line.split(token)[1].split("\"")[1]);
                        } catch (Exception e) {
                                CorruptDataException();
                        }
                }

                return -1;
        }

        /**
         * @param line  line to search token for
         * @param token string to search for in line
         * @return string following directly after token
         */
        private static String readStringAfterToken(String line, String token) {
                if (line.contains(token)) {
                        return line.split(token)[1].split("<")[0];
                }

                return null;
        }

        /**
         * Method responsible for getting the splits over 1Km, 1 Mile, the elevations of each split. Stores this data in a Split object.
         */
        public static void totalDistanceAndTimes() {
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

                for (int i = 0; i < positions.size() - 1; i++) {
                        distanceTemp = haversine(positions.get(i).getLatitude(), positions.get(i + 1).getLatitude(), positions.get(i).getLongitude(), positions.get(i + 1).getLongitude());
                        runningSplitDistanceKm += distanceTemp;
                        runningSplitDistanceMile += distanceTemp;
                        runningDistance += distanceTemp;

                        timeTemp = timeBetweenTwoPositions(positions.get(i), positions.get(i + 1));
                        runningSplitTimeKm += timeTemp;
                        runningSplitTimeMile += timeTemp;
                        runningTime += timeTemp;

                        currElevChange = (positions.get(i + 1).getElevation() - positions.get(i).getElevation());
                        runningElevKm += currElevChange;
                        runningElevMile += currElevChange;

                        //For getting the average speed over the last 25 meters of a split (For readjusting inaccurate distance traveled)
                        if (runningSplitDistanceKm > 975) {
                                endOfSplitTimeKm += timeTemp;
                                endOfSplitDistanceKm += distanceTemp;

                                if (runningSplitDistanceKm > GPSMASTER_KILOMETER) {
                                        double extraTimeToCarry = adjustTimeTakenOverDistance(runningSplitDistanceKm - GPSMASTER_KILOMETER, endOfSplitTimeKm, endOfSplitDistanceKm);
                                        double realSplitTime = runningSplitTimeKm - extraTimeToCarry;

                                        splits.put(splitIndex, new Split(realSplitTime, GPSMASTER_KILOMETER, runningElevKm, false));
                                        runningSplitTimeKm = extraTimeToCarry;
                                        runningSplitDistanceKm = runningSplitDistanceKm - GPSMASTER_KILOMETER;
                                        runningElevKm = 0;
                                        splitIndex++;
                                }
                        }

                        //Done for the same as done above for the Km splits
                        if (runningSplitDistanceMile > 1584.34) { //use constans for mile or Km
                                endOfSplitTimeMile += timeTemp;
                                endOfSplitDistanceMile += distanceTemp;

                                if (runningSplitDistanceMile > GPSMASTER_MILE) {
                                        double extraTimeToCarry = adjustTimeTakenOverDistance(runningSplitDistanceMile - GPSMASTER_MILE, endOfSplitTimeMile, endOfSplitDistanceMile);
                                        double realSplitTime = runningSplitTimeMile - extraTimeToCarry;

                                        splits.put(splitIndex, new Split(realSplitTime, GPSMASTER_MILE, runningElevMile, false));
                                        runningSplitTimeMile = extraTimeToCarry;
                                        runningSplitDistanceMile = runningSplitDistanceMile - GPSMASTER_MILE;
                                        runningElevMile = 0;
                                        splitIndex++;
                                }
                        }
                }

                splits.put(splitIndex++, new Split(runningSplitTimeKm + getProjectedTimeForUnfinishedSplit(runningSplitDistanceKm, GPSMASTER_KILOMETER, runningSplitTimeKm), GPSMASTER_KILOMETER, runningElevKm, true));
                splits.put(splitIndex++, new Split(runningSplitTimeMile + getProjectedTimeForUnfinishedSplit(runningSplitDistanceMile, GPSMASTER_MILE, runningSplitTimeMile), GPSMASTER_MILE, runningElevMile, true));
                overallDistance = runningDistance;
                overallTime = runningTime;
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
                int hrs = Integer.parseInt(timeString.substring(11, 13));
                int mins = Integer.parseInt(timeString.substring(14, 16));
                float secs = Float.parseFloat(timeString.substring(17, 19));

                return hrs + ":" + mins + ":" + secs;
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
                double avgSpeed = (distanceSoFar / GPSMASTER_KILOMETER) / (timeSoFar / 60 / 60);
                return (wantedDistance - distanceSoFar) / (((avgSpeed / 60) / 60) * GPSMASTER_KILOMETER);
        }

        //TODO: use average speed over last, say, 50 meters not the entire split! ::Done

        /**
         * Not fully possible to get time taken over exactly 1km or 1mile with data so this method gets the extra distance traveled
         * over the required distance and works how far you've traveled based on average speed
         *
         * @param extraDistanceTraveled Extra distance traveled over split
         * @return Time to subtract from this split
         */
        private static double adjustTimeTakenOverDistance(double extraDistanceTraveled, double time, double distance) {
                double avgSpeed = distance / time;
                return extraDistanceTraveled / (((avgSpeed / 60) / 60) * GPSMASTER_KILOMETER);
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
}