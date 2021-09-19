package SmartCity;

import SmartCity.RPCServices.DroneRPCSendingService;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RechargeBattery extends Thread {
    Drone target;
    Drone sender;
    String timestamp;

    public RechargeBattery(Drone sender, Drone target, String timestamp){
        this.sender = sender;
        this.target = target;
        this.timestamp = timestamp;
    }


    public void run(){
        System.out.println("[BATTERY] Sending request to " + target.getId());
        boolean response = DroneRPCSendingService.sendRechargeRequest(sender, target, timestamp);
        if(response){
             sender.removeDroneToRecharge(target);
            System.out.println("[BATTERY] " + target.getId() + " replyed OK");

            synchronized (sender.getWaitForRecharge()){
                sender.getWaitForRecharge().notify();
            }

        }else{
            System.out.println("[BATTERY] Waiting " + target.getId());
        }

    }
}
