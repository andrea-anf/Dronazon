package SmartCity;

import Amministrazione.Coordinates;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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
    private boolean partecipation;


    public Drone (){}

    public ClientResponse connect(){

        ClientConfig config = new DefaultClientConfig();
        config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        Client client = Client.create(config);
        WebResource webResource = client.resource("http://localhost:1338/drones/addDrone");

        String input = "{\"ID\": \""+this.id+"\","+
                "\"local port\":\""+this.localPort+"\"," +
                "\"local address\":\""+this.localAddress+"\"}";

        ClientResponse response = webResource.type("application/json").post(ClientResponse.class, input);
        return response;
    }

    //Returns the smartcity, operated before connect() to verify if it's empty
    public ClientResponse getSmartCity() {
        Client client = Client.create();

        WebResource webResource = client.resource("http://localhost:1338/admin/getSmartCity");
        ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

        return response;
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
}

