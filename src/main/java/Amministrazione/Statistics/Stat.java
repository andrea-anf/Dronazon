package Amministrazione.Statistics;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Stat {
    @XmlElement(name = "avgDeliveries")
    private double deliveriesCount;
    @XmlElement(name = "avgKmTraveled")
    private double kilometers;
    @XmlElement(name = "avgAirPollution")
    private double pollutionLevel;
    @XmlElement(name = "avgBatteryLeft")
    private double batteryAvg;
    @XmlElement(name = "timestamp")
    private String timestamp;

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

    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String ts) {
        this.timestamp = ts;
    }
}


