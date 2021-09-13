package SmartCity.MasterDrone;

import SmartCity.Drone;
import com.sun.jersey.api.client.ClientResponse;

public class StatsSender extends Thread{
    private Drone master;

    public StatsSender(Drone master){
        this.master = master;
    }

    public void run(){
        while(true){
            if(master.isMaster()){
                if(master.getStats().getAvgKmTraveled() > 0){
                    System.out.println("\n[STATISTICS]" +
                            "\n\tAverage air pollution: " + master.getStats().getAvgAirPollution() +
                            "\n\tAverage km traveled: " + master.getStats().getAvgKmTraveled() +
                            "\n\tAverage battery left: " + master.getStats().getAvgBatteryLeft() +
                            "\n\tAverage delivery completed: " + master.getStats().getAvgDelivery(master.getDronelist()));


                    System.out.println("\n[STATISTICS] Sending global statistics to the server");

                    ClientResponse response = master.sendStats();
                    if (response.getStatus() != 200) {
                        throw new RuntimeException("[SERVER] Failed : HTTP error code : " + response.getStatus());
                    }

                    System.out.print("[STATISTICS]  " + response);

                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            // implemented the else just to make other drones prints the stats
            // without writing another thread
            else{
                if(master.getDeliveryCompleted() > 0){
                    System.out.println("\n[STATISTICS]" +
                            "\n\tAverage delivery completed: " + master.getDeliveryCompleted() +
                            "\n\tAverage km traveled: " + master.getKmTraveled() +
                            "\n\tAverage battery left: " + master.getBatteryLevel());
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }
}