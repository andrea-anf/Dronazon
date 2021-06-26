package SmartCity.MasterDrone;

import Amministrazione.Coordinates;

public class Order {
    private String id;
    private Coordinates departure;
    private Coordinates destination;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Coordinates getDeparture() {
        return departure;
    }

    public void setDeparture(Coordinates departure) {
        this.departure = departure;
    }

    public Coordinates getDestination() {
        return destination;
    }

    public void setDestination(Coordinates destination) {
        this.destination = destination;
    }
}
