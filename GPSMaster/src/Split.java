/**
 * Created by Gary on 04/01/2016.
 */
public class Split {
    private double split;
    private double time;
    private boolean isProjectedSplit;

    public Split(double time, double split, boolean isProjectedSplit) {
        this.time = time;
        this.split = split;
        this.isProjectedSplit = isProjectedSplit;
    }

    private String getSpeed() {
        double speed = (split/1000)/(time/60/60);
        return String.format("%.2f", speed);
    }

    private String getPace() {
        String str ="";
        double secsToMins = time/60;
        int mins = 0;

        while(secsToMins > 1) {
            mins += 1;
            secsToMins -= 1;
        }
        secsToMins *= 60;

        //add leading zero if int is less than 10
        return mins+":"+((secsToMins < 10) ? ("0" + (int)secsToMins) : (int)secsToMins);
    }

    public double getTime() {
        return time;
    }

    public double getSplit() {
        return split;
    }

    public boolean isProjectedSplit() { return isProjectedSplit; }

    //TODO: get elevation
    public String toString() {
        String format = "%4s|%4s%8s%7s|%1s%8s%5s|%2s";
        if(isProjectedSplit) {
            return String.format(format, " ", " ", "(P)"+getSpeed(), " "," ", "(P)"+getPace(), " ", " ");
        }
        else {
            return String.format(format, " ", " ",""+getSpeed(), " "," ", ""+getPace()," ", " ");
        }
    }
}
