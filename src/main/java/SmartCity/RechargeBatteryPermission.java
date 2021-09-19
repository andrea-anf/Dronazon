package SmartCity;

import SmartCity.RPCServices.DroneRPCSendingService;

public class RechargeBatteryPermission extends Thread{
    Drone target;
    Drone sender;
    public RechargeBatteryPermission(Drone sender, Drone target){
        this.target = target;
        this.sender = sender;
    }

    public void run(){
        System.out.println("[BATTERY] Sending OK to " + target.getId());
        DroneRPCSendingService.sendRechargePermission(sender, target);

    }
}
