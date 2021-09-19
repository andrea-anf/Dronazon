package SmartCity;

import Amministrazione.Coordinates;
import SmartCity.BatteryRecharge.RechargeBattery;
import SmartCity.BatteryRecharge.RechargeBatteryPermission;
import SmartCity.SensoreInquinamento.Buffer;
import SmartCity.SensoreInquinamento.Measurement;
import SmartCity.MasterDrone.Statistics;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.*;
import java.sql.Timestamp;
import java.util.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Drone {
    @XmlElement(name = "ID")
    private int id;
    @XmlElement(name = "local port")
    private String localPort;
    @XmlElement(name = "local address")
    private String localAddress;
    @XmlElement(name = "local coords")
    private Coordinates coords = new Coordinates();

    //delivery stats
    private String arrivalTime;
    private int batteryLevel = 80;
    private double kmTraveled = 0;
    private int deliveryCompleted = 0;
    private Statistics statistics = new Statistics();

    //ring information
    private Drone masterDrone;
    private boolean partecipation = false;
    private String serverAddress = "http://localhost:1338/";
    private boolean quitting = false;
    private Drone nextDrone = null;
    private Drone nextNextDrone = null;
    private boolean masterPrevDrone = false;
    private boolean recovery = false;

    //master drone attributes
    private boolean master = false;
    private MqttClient client;
    private List<Drone> dronelist = new ArrayList<>();
    private Queue<String> orderQueue = new LinkedList<>();
    private boolean delivering = false;

    //recharge attributes
    private boolean recharging = false;
    private boolean wantsToRecharge = false;
    private Timestamp timestampToRecharge;
    private List<Drone> dronesToRecharge = new ArrayList<>();
    private Queue<Drone> dronesToOkRecharge = new LinkedList<>();

    //locks
    private final Object deliveryLock = new Object();
    private final Object dronelistLock = new Object();
    private final Object waitForRecharge = new Object();
    private final Object waitForResponseRecharge = new Object();



    public Drone (){}




    //HTTP REQUESTS
    public ClientResponse connect(){
        Client client = Client.create();
        WebResource webResource = client.resource(serverAddress+"drones/addDrone");

        String input = "{\"ID\": \""+this.id+"\","+
                "\"local port\":\""+this.localPort+"\"," +
                "\"local address\":\""+this.localAddress+"\"}";

        //make a post request to add drone to ServerAmministratore
        return webResource.type("application/json").post(ClientResponse.class, input);
    }

    public ClientResponse disconnect(){
        Client client = Client.create();
        WebResource webResource = client.resource(serverAddress+"drones/deleteDrone/" + this.getId());
        return webResource.delete(ClientResponse.class);
    }

    public ClientResponse getSmartCity() {
        Client client = Client.create();
        WebResource webResource = client.resource(serverAddress + "admin/getSmartCity");
        return webResource.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    }

    public ClientResponse sendStats(){
        Client client = Client.create();
        WebResource webResource = client.resource(serverAddress+"drones/addStats/");

        String timestamp = new Timestamp(System.currentTimeMillis()).toString();

        String input =
                "{\"avgKmTraveled\": \"" + this.getStats().getAvgKmTraveled()+"\","+
                "\"avgAirPollution\":\"" + this.getStats().getAvgAirPollution()+"\"," +
                "\"avgBatteryLeft\":\"" + this.getStats().getAvgBatteryLeft()+"\"," +
                "\"avgDeliveries\":\"" + this.getStats().getAvgDelivery(dronelist) +"\"," +
                "\"timestamp\":\"" + timestamp +"\"}";

        //make a post request to add drone to ServerAmministratore
        return webResource.type("application/json").post(ClientResponse.class, input);
    }




    //ORDER QUEUE
    public Queue<String> getOrderQueue() {
        return orderQueue;
    }
    public String takeOneOrderQueue() {
        return orderQueue.remove();
    }
    public void addOrderQueue(String orderQueue) {
        this.orderQueue.add(orderQueue);
    }


    // ID
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }


    // ADDRESS
    public String getLocalAddress() {
        return localAddress;
    }
    public void setLocalAddress(String localAddress) {
        this.localAddress = localAddress;
    }


    // PORT
    public String getLocalPort() {
        return localPort;
    }
    public void setLocalPort(String localPort) {
        this.localPort = localPort;
    }


    // COORDINATES
    public Coordinates getCoords() {
        return coords;
    }
    public void setCoords(Coordinates coords) {
        this.coords = coords;
    }


    // RING INFORMATION
    public boolean isPartecipation() {
        return partecipation;
    }
    public void setPartecipation(boolean partecipation) {
        this.partecipation = partecipation;
    }

    public Drone getNextDrone() {
        return nextDrone;
    }
    public void setNextDrone(Drone nextDrone0) {
        this.nextDrone = nextDrone0;
    }

    public Drone getNextNextDrone() {
        return nextNextDrone;
    }
    public void setNextNextDrone(Drone nextNextDrone) {
        this.nextNextDrone = nextNextDrone;
    }

    public boolean isMasterPrevDrone() {
        return masterPrevDrone;
    }
    public void setMasterPrevDrone(boolean masterPrevDrone) {
        this.masterPrevDrone = masterPrevDrone;
    }

    public boolean isRecovery() {
        return recovery;
    }
    public void setRecovery(boolean recovery) {
        this.recovery = recovery;
    }

    // SERVER ADDRESS
    public String getServerAddress() {
        return serverAddress;
    }
    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }


    // MASTER INFO
    public boolean isMaster() {
        return master;
    }
    public void setMaster(boolean master) {
        this.master = master;
    }

    public Drone getMasterDrone() {
        return masterDrone;
    }
    public void setMasterDrone(Drone masterDrone) {
        this.masterDrone = masterDrone;
    }

    public MqttClient getClient() {
        return client;
    }
    public void setClient(MqttClient client) {
        this.client = client;
    }


    //DRONELIST
    public List<Drone> getDronelist() {
        return dronelist;
    }
    public synchronized void removeFromDronelist(Drone drone) {
        this.dronelist.remove(drone);
    }
    public synchronized void addToDronelist(Drone drone){
        this.dronelist.add(drone);
    }
    public Drone getById(int id){
        for(Drone d : this.dronelist){
            if(d.getId() == id){
                return d;
            }
        }
        return null;
    }
    public void showDroneList(){
        System.out.println("\n[RING - " + this.getId() +"] SmartCity:");
        for(Drone d : this.getDronelist()){
            System.out.print(
                    "\tID: " + d.getId() +
                            "\t| Coords: (" + d.getCoords().getX()+","+ d.getCoords().getY() + ")" +
                            "\tAddress: " + d.getLocalAddress()+
                            "\tPort: " + d.getLocalPort());

            if(this.getMasterDrone() != null && d.getId() == this.getMasterDrone().getId()){
                if(d.getId() == this.getNextDrone().getId()){
                    System.out.print("\t [MASTER] [NEXT DRONE]\n");
                }
                else if(this.getNextNextDrone() != null && d.getId() == this.getNextNextDrone().getId()){
                    System.out.print("\t [MASTER] [NEXT NEXT DRONE]\n");
                }
                else{
                    System.out.print("\t [MASTER]\n");
                }
            }
            else{
                if(this.getNextDrone() != null && d.getId() == this.getNextDrone().getId()){
                    System.out.print("\t [NEXT]\n");
                }
                else if(this.getNextNextDrone() != null && d.getId() == this.getNextNextDrone().getId()){
                    System.out.print("\t [NEXT NEXT DRONE]\n");
                }
                else{
                    System.out.print("\n");
                }


            }
        }
    }


    //DELIVERING STATE
    public boolean isDelivering() {
        return delivering;
    }
    public void setDelivering(boolean delivering) {
        this.delivering = delivering;
    }


    //STATISTICS
    public Statistics getStats() {
        return statistics;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }
    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public double getKmTraveled() {
        return kmTraveled;
    }
    public void setKmTraveled(double kmTraveled) {
        this.kmTraveled = kmTraveled;
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }
    public void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public int getDeliveryCompleted() {
        return deliveryCompleted;
    }
    public void increseDeliveryCompleted() {
        this.deliveryCompleted++;
    }

    public boolean isQuitting() {
        return quitting;
    }
    public void setQuitting(boolean quitting) {
        this.quitting = quitting;
    }

    Buffer buff = new Buffer() {
        List<Measurement> misures = new ArrayList<>();

        @Override
        public void addMeasurement(Measurement m) {

            misures.add(m);
        }

        @Override
        public List<Measurement> readAllAndClean() {
            List<Measurement> mis = new ArrayList<>();
            //sliding windows with 50% overlap and window with size=8
            for(int i=0; i<8; i++){
                mis.add(misures.get(i));
                if(i < 4){
                    misures.remove(i);
                }
            }
            return mis;
        }
    };
    public Buffer getBuff() {
        return buff;
    }


    //RECHARGE
    public boolean isRecharging() {
        return recharging;
    }
    public void setRecharging(boolean reacharging) {
        this.recharging = reacharging;
    }

    public boolean isWantsToRecharge() {
        return wantsToRecharge;
    }
    public void setWantsToRecharge(boolean wantsToRecharge) {
        this.wantsToRecharge = wantsToRecharge;
    }

    public void rechargeDrone() throws InterruptedException {
        this.setWantsToRecharge(true);
        this.setTimestampToRecharge(new Timestamp(System.currentTimeMillis()));

        for(Drone d : this.getDronelist()){
            if(d.getId() != this.getId()){
                this.addDroneToRecharge(d);
                Thread recharger = new RechargeBattery(this, d, this.getTimestampToRecharge().toString());
                recharger.start();
            }
        }

        synchronized (this.getWaitForRecharge()){
            while(this.getDronesToRecharge().size() > 0){
                System.out.println("[BATTERY] Waiting for " + this.getDronesToRecharge().size() + " drones");
                try {
                    this.getWaitForRecharge().wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            this.setRecharging(true);
            System.out.println("[BATTERY] Recharging battery");

            Thread.sleep(10000);

            this.setBatteryLevel(100);
            System.out.println("[BATTERY] Battery is full");

            this.setWantsToRecharge(false);
            this.setRecharging(false);

            while(this.getDronesToOkRecharge().size() > 0){
                Thread recharger = new RechargeBatteryPermission(this, this.removeDronesToOkRecharge());
                recharger.start();
            }


            synchronized (this.getWaitForResponseRecharge()){
                while(this.getDronesToOkRecharge().size() > 0){
                    this.getWaitForResponseRecharge().wait();
                }
            }

        }
    }

    public synchronized List<Drone> getDronesToRecharge() {
        return dronesToRecharge;
    }
    public void setDronesToRecharge(List<Drone> dronesToRecharge) {
        this.dronesToRecharge = new ArrayList<Drone>(dronesToRecharge);
    }
    public synchronized void removeDroneToRecharge(Drone drone) {
        this.dronesToRecharge.remove(drone);
    }
    public synchronized void addDroneToRecharge(Drone drone) {
        this.dronesToRecharge.add(drone);
    }
    public Queue<Drone> getDronesToOkRecharge() {
        return dronesToOkRecharge;
    }

    public Drone removeDronesToOkRecharge() {
        return dronesToOkRecharge.remove();
    }
    public void addDroneToOkRecharge(Drone drone) {
        this.dronesToOkRecharge.add(drone);
    }

    public Timestamp getTimestampToRecharge() {
        return timestampToRecharge;
    }
    public void setTimestampToRecharge(Timestamp timestampToRecharge) {
        this.timestampToRecharge = timestampToRecharge;
    }

    //LOCKS
    public Object getDeliveryLock() {
        return deliveryLock;
    }
    public Object getDronelistLock() {
        return dronelistLock;
    }
    public Object getWaitForRecharge() {
        return waitForRecharge;
    }
    public Object getWaitForResponseRecharge() {
        return waitForResponseRecharge;
    }

    public void showDroneStatus(){

    }

    //TERMINATING DRONES
    public void quitDrone() throws MqttException, InterruptedException {

        System.out.println("[DRONE] Drone " + this.getId() + " want to quit");

        //wait delivery finish to quit
        synchronized (this.getDeliveryLock()){
            while(this.isDelivering()){
                this.getDeliveryLock().wait();
            }
            this.setDelivering(true);

            if(this.isMaster()){
                //disconnect from broker
                if(this.getClient().isConnected()){
                    this.getClient().disconnect();
                    System.out.println("[BROKER] Master Drone " +
                            this.getId() +
                            " disconnected from broker " +
                            client.getServerURI());
                }

//                if(this.getOrderQueue().size() > 0){
//                    synchronized (this.getDronelistLock()) {
//                        while (this.getDronelist().size() < 2) {
//                                System.out.println("\n[QUIT] Waiting for drones to send reamining orders: ");
//                                int i=1;
//                                for(String order : this.getOrderQueue()){
//                                    System.out.println(i+"# " + order);
//                                }
//                                this.getDronelistLock().wait();
//
//                        }
//
//                        System.out.println("\n[QUIT] Sending orders from queue");
//                        while(this.getOrderQueue().size()>0){
//                            DispatchingService disService = new DispatchingService(this.getDronelist());
//                            String o = this.getOrderQueue().peek();
//                            disService.checkAndSendOrder(this, this.takeOneOrderQueue());
//                            try {
//                                Thread.sleep(3000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//
//                }

                System.out.println("\n[STATISTICS]" +
                        "\n\tAverage air pollution: " + this.getStats().getAvgAirPollution() +
                        "\n\tAverage km traveled: " + this.getStats().getAvgKmTraveled() +
                        "\n\tAverage battery left: " + this.getStats().getAvgBatteryLeft() +
                        "\n\tAverage delivery completed: " + this.getStats().getAvgDelivery(this.getDronelist()));


                System.out.println("\n[STATISTICS] Sending global statistics to the server");

                ClientResponse response = this.sendStats();
                if (response.getStatus() != 200) {
                    throw new RuntimeException("[STATISTICS] Failed : HTTP error code : " + response.getStatus());
                }

                System.out.println("[STATISTICS] " + response);

            }

            System.out.println("\n[DRONE] Sending request to quit to the server");
            ClientResponse deleteResponse = this.disconnect();
            System.out.println("[DRONE]: " + deleteResponse);
            System.exit(0);
        }
    }

}

