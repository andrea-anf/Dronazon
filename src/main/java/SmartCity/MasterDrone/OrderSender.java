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
        System.out.println("[!!] Drone " +drone.getId() + " is delivering: " + drone.isDelivering());
        int responseDrone;
        try {
            responseDrone = DroneRPCSendingService.sendOrderRequest(master, drone, order);
        }catch (StatusRuntimeException sre){
            System.out.println("EXCEPTIONNNNNN");
            responseDrone = -1;
        }
        drone.setDelivering(false);
        if(responseDrone > 0){
            if(responseDrone != master.getId()){
                master.getDronelist().remove(drone);
            }
        }else if (responseDrone < 0){
            master.getDronelist().remove(drone);
            master.addOrderQueue(order);
        }
    }

    public boolean isEndSending() {
        return endSending;
    }
}
