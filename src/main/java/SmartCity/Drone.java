package SmartCity;

import Amministrazione.Coordinates;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.*;

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

    private boolean master;
    private String masterAddress;
    private String masterPort;
    private boolean partecipation;
    private int batteryLevel;
    private String serverAddress = "http://localhost:1338/";


    public Drone (){}

    public ClientResponse connect(){

        Client client = Client.create();
        WebResource webResource = client.resource(serverAddress+"drones/addDrone");

        String input = "{\"ID\": \""+this.id+"\","+
                "\"local port\":\""+this.localPort+"\"," +
                "\"local address\":\""+this.localAddress+"\"}";

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

    //    #### ID ####
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    //    #### PORT ####
    public String getLocalPort() {
        return localPort;
    }
    public void setLocalport(String localPort) {
        this.localPort = localPort;
    }

    //    #### ADDRESS ####
    public String getLocalAddress() {
        return localAddress;
    }
    public void setLocalAddress(String localAddress) {
        this.localAddress = localAddress;
    }

    //    #### COORDINATES ####
    public Coordinates getCoords() {
        return coords;
    }
    public void setCoords(Coordinates coords) {
        this.coords = coords;
    }

    public void setLocalPort(String localPort) {
        this.localPort = localPort;
    }

    public boolean isMaster() {
        return master;
    }

    public void setMaster(boolean master) {
        this.master = master;
    }

    public boolean isPartecipation() {
        return partecipation;
    }

    public void setPartecipation(boolean partecipation) {
        this.partecipation = partecipation;
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }
    public void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

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
}

