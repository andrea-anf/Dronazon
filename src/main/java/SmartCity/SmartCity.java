package SmartCity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SmartCity {
    private ArrayList<Drone> dronelist;
    private static SmartCity instance;

    private SmartCity() {
        dronelist = new ArrayList<>();
    }

    //singleton class
    public synchronized static SmartCity getInstance() {
        if (instance == null) {
            instance = new SmartCity();
        }
        return instance;
    }

    public synchronized boolean addDrone(Drone drone) {
        if (dronelist.contains(drone)) {
            return false;
        } else {
            dronelist.add(drone);
            return true;
        }
    }

    public synchronized Drone getById(int drone) {
        ArrayList<Drone> dronelistCopy = getDronelist();

        for (Drone d : dronelistCopy) {
            if (d.getId() == drone)
                return d;
        }
        return null;
    }


    public synchronized void deleteDrone(Drone drone) {
        ArrayList<Drone> dronelistCopy = SmartCity.getInstance().getDronelist();

        for (Drone d : dronelistCopy) {
            if (d.getId() == (drone.getId())) {
                dronelist.remove(d);
            }
        }
    }

    public ArrayList<Drone> getDronelist() {
        return dronelist;
    }
    public void setDronelist(ArrayList<Drone> dronelist) {
        this.dronelist = dronelist;
    }
}
