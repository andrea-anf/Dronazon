package SmartCity;

import SmartCity.MasterDrone.MasterDrone;
import SmartCity.RPCServices.DroneRPCSendingService;
import org.eclipse.paho.client.mqttv3.MqttException;

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

//                System.out.println("[PING] To " + next.getId());
                if (ping(next) == -1) {
                    //Next drone is unreachable, removing it from the dronelist
                    System.out.println("\n[PING] Drone " + next.getId() + " unreachable");
                    sender.removeFromDronelist(next);

                    //if next next drone exists, try to fix
                    if(sender.getNextNextDrone() != null
                            && sender.getById(sender.getNextNextDrone().getId()) != null
                            && sender.getId() != sender.getNextNextDrone().getId()) {

                        Drone nextNext = sender.getById(sender.getNextNextDrone().getId());
                        System.out.println("[PING] Fixing ring with " + nextNext.getId());
                        int fixResult = fixNext(nextNext);
                        sender.showDroneList();

                        if((next.getId() == sender.getMasterDrone().getId()) && fixResult == 0){
                            sender.showDroneList();
                            System.out.println("\n[PING] Master down, starting a new election");
                            sender.setMasterDrone(null);
                            DroneRPCSendingService.sendElection(sender.getId(), sender.getBatteryLevel(), sender.getNextDrone(), "election");

                        }
                        else if(fixResult == -1) {
                            try {
                                sender.quitDrone();
                            } catch (MqttException | InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    else{
                        //if nextNext drone doesn't exists
                        System.out.println("[RING] NextNext Drone doesn't exists");
                        sender.showDroneList();
                        sender.setNextDrone(sender);

                        if(sender.getId() != sender.getMasterDrone().getId()){
                            sender.setMasterPrevDrone(false);
                            Runnable master = new MasterDrone(sender);
                            Thread thread = new Thread(master);
                            thread.start();
                        }
                    }


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
        return pingNextResult;
    }


    int fixNextNext(Drone nextNext) {

        int newNextNextDroneId = DroneRPCSendingService.pingDrone(nextNext, true);

        if (newNextNextDroneId == -1) {
            System.out.println("[PING] Recovery failed [1] Can't reach next drone");
            sender.getDronelist().remove(sender.getById(nextNext.getId()));
            return -1;

        } else if(newNextNextDroneId == sender.getId()){
            System.out.println("[PING] Recovery failed [2] Trying to reach myself");
            sender.getNextNextDrone().setId(0);
            return 0;
        }
        else{
            System.out.println("[PING] Fixing connection between drones [nextNext] ");
            sender.setNextNextDrone(sender.getById(newNextNextDroneId));
            System.out.println("[INFO] NextDrone: " + sender.getNextDrone().getId());
            if(sender.getNextNextDrone() != null) {
                System.out.println("[INFO] NextNextDrone: " + sender.getNextNextDrone().getId());
            }
            else{
                System.out.println("[PING] Failed [1] to fix nextNext drone, retrying ");
            }

            return 0;
        }
    }

    int fixNext(Drone nextNext) {

        int newNextDroneId = DroneRPCSendingService.pingDrone(nextNext, true);

        if (newNextDroneId == -1) {
            System.out.println("[PING] Recovery failed [2.1] Can't reach next drone");
            sender.getDronelist().remove(sender.getById(nextNext.getId()));
            return -1;

        } else if(newNextDroneId == sender.getId()){
            System.out.println("[PING] Fixing [2.1] connection between drones");
            sender.setNextDrone(nextNext);
            sender.setNextNextDrone(sender);

            System.out.println("[INFO] NextDrone: " + sender.getNextDrone().getId());
            System.out.println("[INFO] NextNextDrone: " + sender.getNextNextDrone().getId());
            return 0;
        }
        else{
            System.out.println("[PING] Fixing [2.2] connection between drones");
            sender.setNextDrone(sender.getById(sender.getNextNextDrone().getId()));
            sender.setNextNextDrone(sender.getById(newNextDroneId));

            System.out.println("[INFO] NextDrone: " + sender.getNextDrone().getId());
            System.out.println("[INFO] NextNextDrone: " + sender.getNextNextDrone().getId());

            return 0;
        }
    }
}
