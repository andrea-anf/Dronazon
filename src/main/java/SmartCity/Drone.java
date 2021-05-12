package SmartCity;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Drone {
    private int id;
    private String port;
    private String address;
    private boolean master;
    private boolean partecipation;
    private int batteryLevel;


    public Drone (){
        this.master = false;
        this.partecipation = false;
        this. batteryLevel = 100;
    }

    //    #### PORT ####
    public String getPort() {
        return port;
    }
    public void setPort(String port) {
        this.port = port;
    }

    //    #### ADDRESS ####
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
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
