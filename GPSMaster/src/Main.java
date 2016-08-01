import java.util.Scanner;

/**
 * Created by Gary Mac Elhinney on 30/07/2016.
 */
public class Main {
        public static void main(String[] args) {
                Scanner sc = new Scanner(System.in);
                System.out.println("Enter filename:");
                String fileName = sc.nextLine();

                GPSMaster.readFile(fileName);
                GPSMaster.getGPSPositions();
                GPSMaster.getSplitsFor(1000);
                //GPSMaster.averageSpeed();
                //GPSMaster.averageKMPace();
                //GPSMaster.averageMilePace();
                //GPSMaster.writeInfoToFile();
                /*if (args.length > 0) {
                        GPSMaster.readFile(args[0]);
                        GPSMaster.getGPSPositions();
                        GPSMaster.totalDistanceAndTimes();
                        GPSMaster.averageSpeed();
                        GPSMaster.averageKMPace();
                        GPSMaster.averageMilePace();
                        GPSMaster.writeInfoToFile();
                } else {
                        System.out.println("Error: No command line arugments given");
                }*/
        }
}
