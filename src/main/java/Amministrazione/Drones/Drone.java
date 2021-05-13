package Amministrazione.Drones;

import Amministrazione.Coords;
import com.sun.xml.bind.v2.runtime.Coordinator;

import javax.xml.bind.annotation.XmlRootElement;
import java.awt.Point;

@XmlRootElement
public class Drone {
    private int id;
    private String port;
    private String address;
    Coords coords = new Coords();


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

    public Coords getCoords() {
        return coords;
    }

    public void setCoords(Coords coords) {
        this.coords = coords;
    }
}
