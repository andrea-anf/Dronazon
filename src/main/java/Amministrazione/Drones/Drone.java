package Amministrazione.Drones;

import Amministrazione.Coordinates;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Drone {
    private int id;
    private String port;
    private String address;
    private Coordinates coords = new Coordinates();


    public Drone (){}

    //    #### ID ####
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
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

    //    #### COORDINATES ####

    public Coordinates getCoords() {
        return coords;
    }

    public void setCoords(Coordinates coords) {
        this.coords = coords;
    }
}
