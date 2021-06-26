package SmartCity.MasterDrone;

import SmartCity.Drone;
import SmartCity.RPCServices.DroneRPCSendingService;

public class OrderSender extends Thread{
    private Drone drone;
    private String order;

    public OrderSender(Drone drone, String order){
        this.drone = drone;
        this.order = order;
    }

    public void run(){
        DroneRPCSendingService.sendOrderRequest(drone, order);
    }
}
