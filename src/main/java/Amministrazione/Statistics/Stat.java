package Amministrazione.Statistics;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;


public class Stat {
    private double deliveriesCount;
    private double kilometers;
    private double pollutionLevel;
    private double batteryAvg;
    private Timestamp timestamp;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    public double getDeliveriesCount() {
        return deliveriesCount;
    }
    public void setDeliveriesCount(double deliveriesCount) {
        this.deliveriesCount = deliveriesCount;
    }

    public double getKilometers() {
        return kilometers;
    }
    public void setKilometers(double kilometers) {
        this.kilometers = kilometers;
    }

    public double getPollutionLevel() {
        return pollutionLevel;
    }
    public void setPollutionLevel(double pollutionLevel) {
        this.pollutionLevel = pollutionLevel;
    }

    public double getBatteryAvg() {
        return batteryAvg;
    }
    public void setBatteryAvg(double batteryAvg) {
        this.batteryAvg = batteryAvg;
    }

    public String getTs() {
        return formatter.format(timestamp);
    }
    public void setTs(Timestamp ts) {
        this.timestamp = ts;
    }
}


