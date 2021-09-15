package SmartCity;

import SmartCity.RPCServices.DroneRPCSendingService;

import java.util.List;

public class CheckNext extends Thread{
    private Drone sender;
    private Drone target;
    private List<Drone> dronelist;

    public CheckNext(Drone sender, Drone target){
        this.sender = sender;
        this.target = target;
    }

    public void run(){
        while(true){
            System.out.println("\n[RING] Ping:");
            if(!DroneRPCSendingService.pingDrone(sender.getNextDrone())){
                    System.out.println("\nNext Drone " + target.getId() + " is not reachable");
//                    sender.getDronelist().remove(target);
//                    System.out.println("\tDrone removed from dronelist");
//                    System.out.println("\tMaster not reachable, need to start an election");
            }
            else{
                System.out.println("\tDrone " + target.getId() + " is reachable");
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
