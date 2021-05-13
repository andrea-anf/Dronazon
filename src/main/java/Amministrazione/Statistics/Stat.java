package Amministrazione.Statistics;

public class Stat {
    private int deliveriesCount;
    private double kilometers;
    private double pollutionLevel;
    private double batteryAvg;


    public int getDeliveriesCount() {
        return deliveriesCount;
    }
    public void setDeliveriesCount(int deliveriesCount) {
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
}
