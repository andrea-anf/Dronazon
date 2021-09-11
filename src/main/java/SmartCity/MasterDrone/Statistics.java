package SmartCity.MasterDrone;

import SmartCity.Drone;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Statistics {

    @XmlElement(name = "avgDeliveries")
    private double avgDelivery = 0;
    @XmlElement(name = "avgKmTraveled")
    private double avgKmTraveled = 0;
    @XmlElement(name = "avgAirPollution")
    private double avgAirPollution = 0;
    @XmlElement(name = "avgBatteryLeft")
    private double avgBatteryLeft = 0;
    @XmlElement(name = "timestamp")
    private String ts;

    private List<Double> deliveryList = new ArrayList<>();
    private List<Double> kmTraveledList = new ArrayList<>();
    private List<Double> airPollutionList = new ArrayList<>();
    private List<Double> batteryLeftList = new ArrayList<>();

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public void addToBatteryLeftList(double batteryLeftAmount){
        this.batteryLeftList.add(batteryLeftAmount);
    }
    public double getAvgBatteryLeft(){
        double count = 0;
        for(double batteryLeft : batteryLeftList){
            count = count + batteryLeft;
        }
        avgBatteryLeft = (count/batteryLeftList.size());
        return avgBatteryLeft;
    }

    public void addToAirPollutionList(double airPollutionAmount){
        this.airPollutionList.add(airPollutionAmount);
    }
    public double getAvgAirPollution(){
        double count = 0;
        for(double pollution : airPollutionList){
            count = count + pollution;
        }
        avgAirPollution = (count/airPollutionList.size());
        return avgAirPollution;
    }

    public void addToDeliveryList(double deliveryAmount) {
        this.deliveryList.add(deliveryAmount);
    }
    public double getAvgDelivery(List<Drone> dronelist){
        double count = 0;
        for(Drone drone : dronelist){
            count += drone.getDeliveryCompleted();
        }
        avgDelivery = (count/dronelist.size());
        return avgDelivery;
    }

    public void addToKmTraveledList(double kmAmount){
        this.kmTraveledList.add(kmAmount);
    }
    public double getAvgKmTraveled(){
        double count = 0;
        for(double km : kmTraveledList){
            count = count + km;
        }
        avgKmTraveled = (count/ kmTraveledList.size());
        return avgKmTraveled;
    }

    public double readAvgDelivery(){
        return avgDelivery;
    }


}
