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
        while(true){
            pingAndFix();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        while (true) {
//            System.out.println("\n[RING] Ping:");
//
//            //DOWN CASE
//            if (!DroneRPCSendingService.pingDrone()) {
//                //MASTER CASE
//                if (sender.getNextDrone().getId() == sender.getMasterDrone().getId()) {
//                    System.out.println("\n\tNext Drone " + sender.getNextDrone().getId() + "[MASTER] DOWN");
//                    System.out.println("\tMaster not reachable, need to start an election");
//                    DroneRPCSendingService.sendElection(sender.getId(), sender.getNextNextDrone(), "election");
//                    sender.setPartecipation(true);
//                    //wait until the end of election
////                    synchronized (sender.getWaitForElectionLock()) {
////                        while (sender.isPartecipation()) {
////                            try {
////                                sender.getWaitForElectionLock().wait();
////                            } catch (InterruptedException e) {
////                                e.printStackTrace();
////                            }
////                        }
////                        sender.setPartecipation(false);
////                    }
//                }
//                System.out.println("\n\tDrone " + sender.getNextDrone().getId() + " DOWN");
//                sender.setNextDrone(sender.getNextNextDrone());
//
//                //if next drone is down, pings nextNext Drone, and if it's not reachable and it was master, starts an election
//                if (!DroneRPCSendingService.pingDrone(sender.getNextNextDrone())) {
//                    if (sender.getNextNextDrone().getId() == sender.getMasterDrone().getId()) {
//                        System.out.println("\n\tNext Next Drone " + sender.getNextNextDrone().getId() + "[MASTER] DOWN");
//                        System.out.println("\tMaster not reachable, but I don't know other drones!!!");
//                    }
//                    System.out.println("\n\tNext Next Drone " + sender.getNextNextDrone().getId() + " DOWN");
//
//               //if NextNext Drone is up, do nothing
//                } else {
//                    if (sender.getNextNextDrone().getId() == sender.getMasterDrone().getId()) {
//                        System.out.println("\tNext Next Drone " + sender.getNextNextDrone().getId() + "[MASTER] UP");
//
//                    } else {
//                        System.out.println("\tNext Next Drone " + sender.getNextDrone().getId() + " UP");
//                    }
//                }
//                //if Next Drone is up, do nothing
//            } else {
//                if (sender.getNextDrone().getId() == sender.getMasterDrone().getId()) {
//                    System.out.println("\tNext Drone " + sender.getNextDrone().getId() + "[MASTER] UP");
//
//                } else {
//                    System.out.println("\tNext Drone " + sender.getNextDrone().getId() + " UP");
//                }
//            }
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

    }

    public void pingAndFix(){
        if(DroneRPCSendingService.pingDrone(sender.getNextDrone(), false) == -1){
            sender.getDronelist().remove(sender.getNextDrone());

            int nextNextID = DroneRPCSendingService.pingDrone(sender.getNextNextDrone(), true);

            if(nextNextID == -1){
                System.out.println("[PING] No more drones available");
                sender.getDronelist().remove(sender.getNextNextDrone());
//
//                sender.setNextDrone(sender);
//                sender.setNextNextDrone(sender);

                sender.showDroneList();

            }
            else {
                System.out.println("[PING] fixing connection between drones");
                if(sender.getNextDrone() == sender.getMasterDrone()){
                    sender.setMasterDrone(null);
                    DroneRPCSendingService.sendElection(sender.getId(), sender.getNextNextDrone(), "election");
                }
//TODO: perchè diavolo non cancella il master dalla lista?????
                sender.setNextDrone(sender.getNextNextDrone());
                sender.setNextNextDrone(sender.getById(nextNextID));
                sender.showDroneList();

            }
        }

    }
}
