package SmartCity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

public class MasterDronelist {

    private ArrayList<Drone> dronelist;
    private static MasterDronelist instance;

    private MasterDronelist() {
        dronelist = new ArrayList<>();
    }

    //singleton class
    public static MasterDronelist getInstance() {
        if (instance == null) {
            instance = new MasterDronelist();
        }
        return instance;
    }

    public boolean addDrone(Drone drone) {
        if (dronelist.contains(drone)) {
            return false;
        } else {
            dronelist.add(drone);
            return true;
        }
    }

    public Drone getById(int drone) {

        for (Drone d : dronelist) {
            if (d.getId() == drone)
                return d;
        }
        return null;
    }

    //TODO rivedere come usare i synchronized
    public synchronized void  deleteDrone(Drone drone) {
        for (Drone d : dronelist) {
            if (d.getId() == (drone.getId())) {
                dronelist.remove(d);
                break;
            }
        }

    }

    public synchronized ArrayList<Drone> getDronelist() {
        return dronelist;
    }
    public synchronized void setDronelist(ArrayList<Drone> dronelist) {
        this.dronelist = dronelist;
    }
}
