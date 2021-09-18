package SmartCity;

import SmartCity.RPCServices.DroneRPCSendingService;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.List;

public class CheckDrone extends Thread {
    private Drone sender;
    private Drone next;

    public CheckDrone(Drone sender) {
        this.sender = sender;
    }

    public void run() {
        while (true) {
            if(sender.getById(sender.getNextDrone().getId()) != null){
                next = sender.getById(sender.getNextDrone().getId());
                System.out.println("[PING] To " + next.getId());
                if (ping(next) == -1) {
                    sender.removeFromDronelist(next);

                    if(sender.getNextNextDrone() != null && sender.getById(sender.getNextNextDrone().getId()) != null) {
                        Drone nextNext = sender.getById(sender.getNextNextDrone().getId());
                        System.out.println("[PING] Fixing ring with " + nextNext.getId());
                        int fixResult = fix(nextNext);

                        if(next.getId() == sender.getMasterDrone().getId() && fixResult == 0){
                            sender.showDroneList();
                            System.out.println("[PING] Master down, starting a new election");
                            sender.setMasterDrone(null);
                            DroneRPCSendingService.sendElection(sender.getId(), sender.getNextDrone(), "election");

//                            synchronized (sender.getWaitForElectionLock()){
//                                while(sender.getMasterDrone() == null){
//                                    try {
//                                        sender.getWaitForElectionLock().wait();
//                                    } catch (InterruptedException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }
                        }
                        else if(fixResult == -1) {
                            try {
                                sender.quitDrone();
                            } catch (MqttException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    sender.showDroneList();
                }
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public int ping(Drone next) {
        int pingNextResult = DroneRPCSendingService.pingDrone(next, false);

        if (pingNextResult == -1) {
            System.out.println("[PING] Drone " + next.getId() + " unreachable");
        }
        return pingNextResult;
    }


    int fix(Drone nextNext) {

        int nextNextID = DroneRPCSendingService.pingDrone(nextNext, true);

        if (nextNextID == -1) {
            System.out.println("[PING] Recovery failed [1] Can't reach next drone");
            sender.getDronelist().remove(sender.getById(nextNext.getId()));
            return -1;

        } else if(nextNextID == sender.getId()){
            System.out.println("[PING] Recovery failed [2] Trying to reach myself");
            return -1;
        }
        else{
            System.out.println("[PING] Fixing connection between drones");
            sender.setNextDrone(sender.getNextNextDrone());
            sender.setNextNextDrone(sender.getById(nextNextID));
            return 0;
        }
    }
}
