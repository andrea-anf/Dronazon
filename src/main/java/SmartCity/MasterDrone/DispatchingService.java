package SmartCity.MasterDrone;

import SmartCity.Drone;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.List;

public class DispatchingService {
    List<Drone> dronelist;
    String order;
    int depX;
    int depY;


    public DispatchingService(List<Drone> dl){
        this.dronelist = dl;
    }

    private void splitMessage(String message){
        String[] parts = message.split(";");

        //take departure coordinates
        String[] departureCoords  = parts[1].split(",");
        this.depX = Integer.parseInt(departureCoords[0]);
        this.depY = Integer.parseInt(departureCoords[1]);
    }

    private double Distance(int aX, int aY, int bX, int bY){
        double firstOp = Math.pow(bX-aX, 2);
        double secondOp = Math.pow(bY-aY, 2);
        return Math.sqrt(firstOp + secondOp);
    }

    public Drone findClosest(String message){
        splitMessage(message);
        double closerDistance = 100;
        Drone closerDrone = this.dronelist.get(0);

        if(this.dronelist.size() > 1){
            for(Drone d : this.dronelist){
                int droneX = d.getCoords().getX();
                int droneY = d.getCoords().getY();

                //find distance between drone and departure point
                double distanceD = Distance(droneX,droneY,depX,depY);

                for(Drone d1 : this.dronelist){
                    if(d1.getId() == d.getId()) continue;
                    int drone1X = d1.getCoords().getX();
                    int drone1Y = d1.getCoords().getY();

                    double distanceD1 = Distance(drone1X,drone1Y,depX,depY);

                    //looks for minor distance between the two drones with the departure point
                    if(distanceD < distanceD1 && distanceD < closerDistance){
                        closerDistance = distanceD;
                        closerDrone = d;
                    }
                    //if equals, check who has the lowest battery level
                    else if (distanceD == closerDistance && d.getId() != closerDrone.getId()){
                        if (d.getBatteryLevel() > closerDrone.getBatteryLevel()){
                            closerDistance = distanceD;
                            closerDrone = d;
                        }
                        else if (d.getBatteryLevel() == closerDrone.getBatteryLevel()){
                            if(d.getId() < closerDrone.getId()){
                                closerDistance = distanceD;
                                closerDrone = d;
                            }

                        }
                    }
                }


            }
        }
        else{
            return dronelist.get(0);
        }

        return closerDrone;
    }
}
