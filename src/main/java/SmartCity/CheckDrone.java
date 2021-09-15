package SmartCity;

import SmartCity.RPCServices.DroneRPCSendingService;

import java.util.List;

public class CheckDrone extends Thread {
    private Drone sender;
    private Drone target;
    private List<Drone> dronelist;

    public CheckDrone(Drone sender) {
        this.sender = sender;
    }

    public void run() {
        while (true) {
            System.out.println("\n[RING] Ping:");

            //pings next drone, and if it's not reachable and it was master, starts an election
            if (!DroneRPCSendingService.pingDrone(sender.getNextDrone())) {
                if (sender.getNextDrone().getId() == sender.getMasterDrone().getId()) {
                    System.out.println("\n\tNext Drone " + sender.getNextDrone().getId() + "[MASTER] DOWN");
                    System.out.println("\tMaster not reachable, need to start an election");
                }
                System.out.println("\n\tDrone " + sender.getNextDrone().getId() + " DOWN");
                sender.getDronelist().remove(sender.getNextDrone());


                //if next drone is down, pings nextNext Drone, and if it's not reachable and it was master, starts an election
                if (!DroneRPCSendingService.pingDrone(sender.getNextNextDrone())) {
                    if (sender.getNextNextDrone().getId() == sender.getMasterDrone().getId()) {
                        System.out.println("\n\tNext Next Drone " + sender.getNextNextDrone().getId() + "[MASTER] DOWN");
                        System.out.println("\tMaster not reachable, need to start an election");
                    }
                    System.out.println("\n\tNext Next Drone " + sender.getNextNextDrone().getId() + " DOWN");
                    sender.getDronelist().remove(sender.getNextNextDrone());

               //if NextNext Drone is up, do nothing
                } else {
                    if (sender.getNextNextDrone().getId() == sender.getMasterDrone().getId()) {
                        System.out.println("\tNext Next Drone " + sender.getNextNextDrone().getId() + "[MASTER] UP");

                    } else {
                        System.out.println("\tNext Next Drone " + sender.getNextDrone().getId() + " UP");
                    }
                }
                //if Next Drone is up, do nothing
            } else {
                if (sender.getNextDrone().getId() == sender.getMasterDrone().getId()) {
                    System.out.println("\tNext Drone " + sender.getNextDrone().getId() + "[MASTER] UP");

                } else {
                    System.out.println("\tNext Drone " + sender.getNextDrone().getId() + " UP");
                }
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
