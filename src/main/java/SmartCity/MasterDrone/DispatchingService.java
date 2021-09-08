package SmartCity.MasterDrone;

import SmartCity.Drone;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class DispatchingService {
    private final List<Drone> dronelist;
    private List<Drone> activeDrones = new ArrayList<>();

    private String order;
    private int depX;
    private int depY;

    public DispatchingService(List<Drone> dl){
        this.dronelist = dl;
    }




    //get coordinates
    private void getCoords(String message){
        String[] parts = message.split(";");

        //take departure coordinates
        String[] departureCoords  = parts[1].split(",");
        this.depX = Integer.parseInt(departureCoords[0]);
        this.depY = Integer.parseInt(departureCoords[1]);
    }

    //calculate distance between departure and destionation
    public double Distance(int aX, int aY, int bX, int bY){
        double firstOp = Math.pow(bX-aX, 2);
        double secondOp = Math.pow(bY-aY, 2);
        return Math.round((Math.sqrt(firstOp + secondOp)*100.0))/100.0;
    }

    public void checkAndSendOrder(Drone master, String message){
        int droneId = findClosest(message);
        order = message;
        if(droneId == 0){
            master.addOrderQueue(message);
            System.out.println("\n[QUEUE] Nobody is ready to delivery! Order Queue:");
            int i=0;
            for(String o : master.getOrderQueue()){
                System.out.println("\t#"+i + ": " + o);
                i++;
            }
        }
        else{
            System.out.print("\nSending order " + order + " to ");
            if(droneId == master.getId()){
                System.out.print("Drone " + droneId + "[MASTER]\n");
            }
            else{
                System.out.print("Drone " + droneId + "\n");
            }
            Thread oS = new OrderSender(master, master.getById(droneId), message);
            oS.start();
        }
    }

    public int findClosest(String message){
        Drone closerDrone;
        getCoords(message);
        double closerDistance = 100;

        //find only active drones
        for(Drone drone : this.dronelist){
            if(drone.isDelivering() == false){
                activeDrones.add(drone);
            }
        }
        if(activeDrones.size()==0){
            return 0;
        }
        else if(activeDrones.size() == 1){
            return activeDrones.get(0).getId();
        }
        else {
            closerDrone = activeDrones.get(0);
            for (Drone drone1 : this.activeDrones) {
                //find distance with departure point
                int drone1X = drone1.getCoords().getX();
                int drone1Y = drone1.getCoords().getY();
                double distance1 = Distance(drone1X, drone1Y, this.depX, this.depY);

                for (Drone drone2 : this.activeDrones) {
                    if (drone1.getId() == drone2.getId()) {
                        continue;
                    }
                    int drone2X = drone2.getCoords().getX();
                    int drone2Y = drone2.getCoords().getY();
                    double distance2 = Distance(drone2X, drone2Y, this.depX, this.depY);

                    if (distance1 < distance2) {
                        continue;
                    } else if (distance1 == distance2) {
                        if (drone1.getBatteryLevel() < drone2.getBatteryLevel()) {
                            closerDrone = drone2;
                            closerDistance = distance2;

                        } else if (drone1.getBatteryLevel() == drone2.getBatteryLevel()) {
                            if (drone1.getId() < drone2.getId()) {
                                closerDrone = drone2;
                                closerDistance = distance2;

                            } else {
                                closerDrone = drone1;
                                closerDistance = distance1;
                            }
                        } else {
                            closerDrone = drone1;
                            closerDistance = distance1;
                        }
                    } else {
                        if(distance2 < closerDistance){
                            closerDistance = distance2;
                            closerDrone = drone2;
                        }
                    }
                }

            }
        }

        return closerDrone.getId();
    }

}
