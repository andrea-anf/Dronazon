package SmartCity.MasterDrone;

import SmartCity.Drone;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class DispatchingService {
    MasterDronelist dronelist;
    String order;
    String id;
    int depX;
    int depY;
    int destX;
    int destY;

    public DispatchingService(MasterDronelist dl, MqttMessage message){
        this.dronelist = dl;
        this.order = message.getPayload().toString();

    }

    private void splitMessage(String message){
        String[] parts = message.split(";");
        //take id
        this.id = parts[0];

        //take departure coordinates
        String[] departureCoords  = parts[1].split(",");
        this.depX = Integer.parseInt(departureCoords[0]);
        this.depY = Integer.parseInt(departureCoords[1]);

        //take destination coordinates
        String[] destinationCoords  = parts[2].split(",");
        this.destX = Integer.parseInt(destinationCoords[0]);
        this.destY = Integer.parseInt(destinationCoords[1]);
    }

    private double Distance(int aX, int aY, int bX, int bY){
        double result = -1;
        double firstOp = Math.pow(bX-aX, 2);
        double secondOp = Math.pow(bY-aY, 2);
        result = Math.sqrt(firstOp + secondOp);
        return result;
    }

    public int findClosest(){
        double closerDistance = 100;
        int closerDrone = -1;

        for(Drone d : this.dronelist.getDronelist()){
            int droneX = d.getCoords().getX();
            int droneY = d.getCoords().getY();

            double distanceD = Distance(droneX,droneY,depX,depY);

            for(Drone d1 : this.dronelist.getDronelist()){
                int drone1X = d.getCoords().getX();
                int drone1Y = d.getCoords().getY();

                double distanceD1 = Distance(drone1X,drone1Y,depX,depY);

                //looks for minor distance
                if(distanceD < distanceD1 && distanceD < closerDistance){
                        closerDistance = distanceD;
                        closerDrone = d.getId();
                    }
                    //if equals, check who has the lowest battery level
                    else if (distanceD == closerDistance && d.getId() != closerDrone){
                        if (d.getBatteryLevel() > dronelist.getById(closerDrone).getBatteryLevel()){
                            closerDistance = distanceD;
                            closerDrone = d.getId();
                        }
                        else if (d.getBatteryLevel() == dronelist.getById(closerDrone).getBatteryLevel()){
                            if(d.getId() <= d1.getId()){
                                closerDistance = distanceD;
                                closerDrone = d.getId();
                            }
                            else{
                                closerDistance = distanceD;
                                closerDrone = d1.getId();
                            }
                        }
                    }
                }


            }

        return closerDrone;
    }
}
