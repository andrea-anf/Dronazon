package SmartCity.MasterDrone;

import SmartCity.Drone;
import SmartCity.RPCServices.DroneRPCSendingService;
import io.grpc.StatusRuntimeException;

public class OrderSender extends Thread{
    private Drone master;
    private Drone drone;
    private String order;
    private boolean endSending = false;

    public OrderSender(Drone master, Drone drone, String order){
        this.master = master;
        this.drone = drone;
        this.order = order;
    }

    public void run(){
        drone.setDelivering(true);
        int responseDrone;
        try {
            //returns 0 if ok
            //returns IDdrone if it need to stops
            //return negative if drone doesn't finish the order
            responseDrone = DroneRPCSendingService.sendOrderRequest(master, drone, order);
        }catch (StatusRuntimeException sre){
            responseDrone = -1;
        }
        drone.setDelivering(false);

        if(responseDrone > 0) {
            if (responseDrone != master.getId()) {
                master.getDronelist().remove(drone);
            }
        }
//        else if (responseDrone < 0){
//            master.getDronelist().remove(drone);
//        }
    }
}
