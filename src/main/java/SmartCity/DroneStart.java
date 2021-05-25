package SmartCity;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Scanner;

public class DroneStart {

    public static void main(String[] args) {
        System.out.println("Starting drone:");
        Drone drone = new Drone();
        System.out.println("\tID: " + drone.getId());
        System.out.println("\tPORT: " + drone.getLocalPort());
        System.out.println("\tBATTERY: " + drone.getBatteryLevel());

//        signUpDrone(drone);

        Client client = Client.create();
        WebResource webResource = client.resource(drone.getServerAddress()+"drones/addDrone");

        boolean checkPort = false;
        do{

            String input = String.format("{\"id\":\"%d\", \"port\":\"%d\", \"address\":\"%s\"}",
                    drone.getId(), drone.getLocalPort(),drone.getLocalAddress());

            ClientResponse response = webResource.type("application/json").post(ClientResponse.class, input);
            System.out.println("[+]   " + response.getStatus());
            if(response.getStatus() == 406){
                drone.setId(drone.getId()+1);
                checkPort = false;
            }
            else{
                checkPort = true;
            }
        }while((checkPort == false));

//        if(smartcity.isEmpty()){
//            System.out.println("    [-] No drones in smartcity");
//        }
//        else{
//            for (Drone d : smartcity){
//                System.out.println("    [-]   "+"Name: " + d.getId() + " Coords: (" + d.getCoords().getX()+", " + d.getCoords().getY()+")");
//            }
//        }

    }

    public void signUpDrone(Drone drone){

    }
}
