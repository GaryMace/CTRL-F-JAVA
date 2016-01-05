/**
 * Created by Gary on 04/01/2016.
 */
public class Split {
    private double split;
    private double time;

    public Split(double time, double split) {
        this.time = time;
        this.split = split;
    }

    private String getSpeed() {
        return String.format("%.2f", (split/1000)/(time/60/60));
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

    //TODO: get elevation
    public String toString() {
        return getSpeed()+"  |  "+getPace()+"  |  ";
    }
}
