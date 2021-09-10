package SmartCity.MasterDrone;

import SmartCity.Drone;

import java.util.ArrayList;
import java.util.List;

public class Statistics {

    private List<Double> deliveryList = new ArrayList<>();
    private List<Double> kmTraveledList = new ArrayList<>();
    private List<Double> airPollutionList = new ArrayList<>();
    private List<Double> batteryLeftList = new ArrayList<>();

    private double avgDelivery = 0;
    private double avgKmTraveled = 0;
    private double avgAirPollution = 0;
    private double avgBatteryLeft = 0;


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
