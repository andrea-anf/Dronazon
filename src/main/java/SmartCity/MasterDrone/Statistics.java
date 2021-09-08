package SmartCity.MasterDrone;

import java.util.ArrayList;
import java.util.List;

public class Statistics {

    private List<Double> deliveryList = new ArrayList<>();
    private List<Double> kmTravelledList = new ArrayList<>();
    private List<Double> airPollutionList = new ArrayList<>();
    private List<Double> batteryLeftList = new ArrayList<>();



    public void addToBatteryLeftList(double batteryLeftAmount){
        this.batteryLeftList.add(batteryLeftAmount);
    }
    public double getAvgBatteryLeft(){
        double count = 0;
        for(double batteryLeft : batteryLeftList){
            count = count + batteryLeft;
        }
        return (count/batteryLeftList.size());
    }

    public void addToAirPollutionList(double airPollutionAmount){
        this.airPollutionList.add(airPollutionAmount);
    }
    public double getAvgAirPollution(){
        double count = 0;
        for(double pollution : airPollutionList){
            count = count + pollution;
        }
        return (count/airPollutionList.size());
    }

    public void addToDeliveryList(double deliveryAmount) {
        this.deliveryList.add(deliveryAmount);
    }
    public double getAvgDelivery(){
        double count = 0;
        for(double delivery : deliveryList){
            count = count + delivery;
        }
        return (count/deliveryList.size());
    }

    public void addToKmTravelledList(double kmAmount){
        this.kmTravelledList.add(kmAmount);
    }
    public double getAvgKmTravelled(){
        double count = 0;
        for(double km : kmTravelledList){
            count = count + km;
        }
        return (count/kmTravelledList.size());
    }
}
