package SmartCity;

import Amministrazione.Coordinates;
import SensoreInquinamento.Buffer;
import SensoreInquinamento.Measurement;
import SensoreInquinamento.PM10Simulator;
import SmartCity.MasterDrone.DispatchingService;
import SmartCity.MasterDrone.Order;
import SmartCity.MasterDrone.Statistics;
import com.sun.corba.se.spi.ior.ObjectKey;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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
    private int batteryLevel = 100;
    private double kmTraveled = 0;
    private int deliveryCompleted = 0;
    private Statistics stats;

    //ring information
    private String masterAddress;
    private String masterPort;
    private boolean partecipation;
    private String serverAddress = "http://localhost:1338/";
    private boolean delivering = false;
    private boolean quitting = false;

    //master drone attributes
    private boolean master;
    private MqttClient client;
    private List<Drone> dronelist = new ArrayList<>();
    private Queue<String> orderQueue = new LinkedList<>();

    //locks
    private final Object deliveryLock = new Object();
    private final Object orderQueueLock = new Object();


    //inquinamento
    Buffer buffer = new Buffer() {
        private List<Measurement> buff= new ArrayList<>();
        @Override
        public void addMeasurement(Measurement m) {
            this.buff.add(m);
        }

        @Override
        public List<Measurement> readAllAndClean() {
            List<Measurement> tempBuff = buff;
            buff.clear();
            return tempBuff;
        }
    };

    PM10Simulator pm10 = new PM10Simulator(buffer);


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

    //Returns the smartcity, operated before connect() to verify if it's empty
    public ClientResponse getSmartCity() {
        Client client = Client.create();
        WebResource webResource = client.resource("http://localhost:1338/admin/getSmartCity");
        return webResource.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
    }

    public ClientResponse sendStats(){
        return null;
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


    // SERVER ADDRESS
    public String getServerAddress() {
        return serverAddress;
    }
    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }


    // MASTER INFO
    public String getMasterAddress() {
        return masterAddress;
    }
    public void setMasterAddress(String masterAddress) {
        this.masterAddress = masterAddress;
    }
    public String getMasterPort() {
        return masterPort;
    }
    public void setMasterPort(String masterPort) {
        this.masterPort = masterPort;
    }
    public boolean isMaster() {
        return master;
    }
    public void setMaster(boolean master) {
        this.master = master;
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
    public void setDronelist(List<Drone> dronelist) {
        this.dronelist = dronelist;
    }
    public void addToDronelist(Drone drone){
        this.dronelist.add(drone);
    }

    //DELIVERING STATE
    public boolean isDelivering() {
        return delivering;
    }
    public void setDelivering(boolean delivering) {
        this.delivering = delivering;
    }

    public Drone getById(int id){
        for(Drone d : this.dronelist){
            if(d.getId() == id){
                return d;
            }
        }
        return null;
    }

    //STATISTICS
    public Statistics getStats() {
        return stats;
    }
    public void setStats(Statistics stats) {
        this.stats = stats;
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


    //AIR POLLUTION PM10
    public void startPm10(PM10Simulator pm10) {
        pm10.start();
    }
    public PM10Simulator getPm10() {
        return this.pm10;
    }


    //LOCKS
    public Object getDeliveryLock() {
        return deliveryLock;
    }

    public Object getOrderQueueLock() {
        return orderQueueLock;
    }

    //TERMINATING DRONES
    public void quitDrone(){

        //wait delivery finish to quit
        synchronized (this.getDeliveryLock()){
            while(this.isDelivering()){
                try {
                    this.getDeliveryLock().wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            ClientResponse deleteResponse = this.disconnect();
            System.out.println("\n[QUIT] RESPONSE: " + deleteResponse);

            System.exit(0);
        }
    }
    public void quitDrone(MqttClient client) throws MqttException {

        //wait delivery finish to quit
        synchronized (this.getDeliveryLock()){
            while(this.isDelivering()){
                try {
                    this.getDeliveryLock().wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //disconnect from broker
            client.disconnect();
            System.out.println("[QUIT] Master Drone " +
                    this.getId() +
                    " disconnected from broker " +
                    client.getServerURI());


            synchronized (this.getOrderQueueLock()) {
                while (this.getOrderQueue().size()>0) {
                    try {
                        this.getOrderQueueLock().wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("\n[QUIT] The drone has sent all the remaining orders");

                ClientResponse deleteResponse = this.disconnect();
                System.out.println("\n[QUIT] RESPONSE: " + deleteResponse);
                System.exit(0);
            }


        }
    }
}

