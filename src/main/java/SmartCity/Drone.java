package SmartCity;

import Amministrazione.Coordinates;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Random;

@XmlRootElement
public class Drone {
    private int id;
    private int localPort;
    private String localAddress;

    private boolean master;
    private boolean partecipation;
    private int batteryLevel;

    private Coordinates coords = new Coordinates();
    private final String serverAddress = "http://localhost:1338/";





    public Drone () {
        Random rand = new Random();
        try {
            ServerSocket sock = new ServerSocket(0);
            this.localPort = sock.getLocalPort();
            this.id = rand.nextInt(100);
        }
        catch (IOException e ){
            e.printStackTrace();
        }

        this.master = false;
        this.partecipation = false;
        this. batteryLevel = 100;
    }

    public void doDeliverie(){
        setBatteryLevel(batteryLevel-10);
    }


    public Coordinates getCoords() {
        return coords;
    }

    public void setCoords(Coordinates coords) {
        this.coords = coords;
    }

    //    #### LOCAL PORT ####
    public int getLocalPort() {
        return localPort;
    }
    public void setLocalPort(int localPort) {
        this.localPort = localPort;
    }
    //    #### LOCAL ADDRESS ####
    public String getLocalAddress() {
        return localAddress;
    }
    public void setLocalAddress(String localAddress) {
        this.localAddress = localAddress;
    }


    //    #### SERVER ADDRESS ####
    public String getServerAddress() {
        return serverAddress;
    }

    //    #### ID ####
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    //    #### MASTER ####
    public boolean isMaster() {
        return master;
    }
    public void setMaster(boolean master) {
        this.master = master;
    }

    //    #### PARTECIPATION ####
    public boolean isPartecipation() {
        return partecipation;
    }
    public void setPartecipation(boolean partecipation) {
        this.partecipation = partecipation;
    }

    //    #### BATTERY ####
    public int getBatteryLevel() {
        return batteryLevel;
    }
    public void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }
}
