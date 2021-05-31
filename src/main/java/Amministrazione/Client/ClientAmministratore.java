package Amministrazione.Client;

import SmartCity.Drone;

import Amministrazione.Statistics.Stat;
import SmartCity.SmartCity;
import com.sun.jersey.api.client.*;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;


import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Scanner;

public class ClientAmministratore {

    public static void main(String[] args) {
        String serverAddress = "http://localhost:1338/admin/";
        String service = "";

        Scanner input = new Scanner(System.in);

        int operation = 0;
        while(operation != 4){
            System.out.println("\n-------------------");
            System.out.println("ADMIN CONTROL PANEL");
            System.out.println("-------------------\n");

            System.out.println("[0]   Show active drones in smart-city");
            System.out.println("[1]   Show the last N global statistics");
            System.out.println("[2]   Show the average number of deliveries made");
            System.out.println("[3]   Show the average number of kilometers traveled");
            System.out.println("[4]   Exit");

            operation= input.nextInt();
            switch (operation){
                case 0: {
                    service = "getSmartCity";

                    Client client = Client.create();
                    WebResource webResource = client.resource(serverAddress+service);
                    ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

                    System.out.println("[+]   "+clientResponse.toString());

                    SmartCity smartcity = clientResponse.getEntity(SmartCity.class);

                    if(smartcity.getDronelist().isEmpty()){
                        System.out.println("    [-] No drones in smartcity");
                    }
                    else{
                        for (Drone d : smartcity.getDronelist()){
                            System.out.println("    [-]   "+"ID: " + d.getId() + " Coords: (" + d.getCoords().getX()+", " + d.getCoords().getY()+")");
                        }
                    }

                    System.out.println("\nPress enter to return to the menu...");
                    Scanner wait = new Scanner(System.in);
                    wait.nextLine();

                }
                break;
                case 1: {
                    Scanner input2 = new Scanner(System.in);
                    service = "getLastNStats/";
                    int n;

                    System.out.println("[+] Please define how many stats you want to see: ");
                    n = input2.nextInt();

                    ClientConfig clientConfig = new DefaultClientConfig();
                    clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
                    clientConfig.getClasses().add(JacksonJsonProvider.class);
                    Client client = Client.create(clientConfig);

                    WebResource webResource = client.resource(serverAddress+service+n);
                    ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

                    System.out.println("[+]   "+clientResponse.toString());

                    List<Stat> statlist = clientResponse.getEntity(new GenericType<List<Stat>>() {});

                    if(statlist.isEmpty()){
                        System.out.println("    [-] No stats available");
                    }
                    else{
                        for (Stat s : statlist){
                            System.out.println("\n    [-]\tTimestamp: " + s.getTs().replaceFirst("T", " at ")
                                    +"\n\t\tDeliveries count: " + s.getDeliveriesCount()
                                    +"\n\t\tKilometers traveled: " + s.getKilometers()
                                    +"\n\t\tPollution level: " + s.getPollutionLevel()
                                    +"\n\t\tBattery average: " + s.getBatteryAvg());
                        }
                    }

                    System.out.println("\nPress enter to return to the menu...");
                    Scanner wait = new Scanner(System.in);
                    wait.nextLine();
                }


                    break;
                case 2: {
                    Scanner input2 = new Scanner(System.in);
                    service = "getAvgDeliveries/";
                    String tMin, tMax;

                    System.out.println("[+] Please define starting timestamp in format 'yyyy-mm-dd hh:mm:ss'");
                    tMin = input2.nextLine();
                    tMin = tMin.replace(" ", "%20");

                    System.out.println("[+] Please define ending timestamp in format 'yyyy-mm-dd hh:mm:ss'");
                    tMax = input2.nextLine();
                    tMax = tMax.replace(" ", "%20");

                    ClientConfig clientConfig = new DefaultClientConfig();
                    clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
                    clientConfig.getClasses().add(JacksonJsonProvider.class);
                    Client client = Client.create(clientConfig);

                    WebResource webResource = client.resource(serverAddress+service+tMin+"/"+tMax);
                    ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

                    System.out.println("[+]   "+clientResponse.toString());

                    String result = clientResponse.getEntity(String.class);
                    result = result.replaceAll("[^a-zA-Z0-9 ^:.]", "");
                    System.out.println("\t[-] " + result);

                    System.out.println("\nPress enter to return to the menu...");
                    Scanner wait = new Scanner(System.in);
                    wait.nextLine();
                }
                    break;
                case 3: {
                    Scanner input2 = new Scanner(System.in);
                    service = "getAvgKilometers/";
                    String tMin, tMax;

                    //gets timestamps and formats properly
                    System.out.println("[+] Please define starting timestamp in format 'yyyy-mm-dd hh:mm:ss'");
                    tMin = input2.nextLine();
                    tMin = tMin.replace(" ", "%20");

                    System.out.println("[+] Please define ending timestamp in format 'yyyy-mm-dd hh:mm:ss'");
                    tMax = input2.nextLine();
                    tMax = tMax.replace(" ", "%20");

                    //handles client enabling jackson to do requests
                    ClientConfig clientConfig = new DefaultClientConfig();
                    clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
                    clientConfig.getClasses().add(JacksonJsonProvider.class);
                    Client client = Client.create(clientConfig);

                    //builds and sends request
                    WebResource webResource = client.resource(serverAddress+service+tMin+"/"+tMax);
                    ClientResponse clientResponse = webResource.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

                    System.out.println("[+]   "+clientResponse.toString());

                    String result = clientResponse.getEntity(String.class);
                    result = result.replaceAll("[^a-zA-Z0-9 ^:.]", "");
                    System.out.println("\t[-] " + result);

                    System.out.println("\nPress enter to return to the menu...");
                    Scanner wait = new Scanner(System.in);
                    wait.nextLine();
                }
                    break;
                case 4: {
                    System.out.println("\nExit from AdminControlPanel");
                }

                break;
                default: System.out.println("\nValore non accettato, uscita dal programma!");
            }
        }
    }

    public static ClientResponse getRequest(Client client, String url){
        WebResource webResource = client.resource(url);
        try {
            return webResource.type("application/json").get(ClientResponse.class);
        } catch (ClientHandlerException e) {
            System.out.println("Server non disponibile");
            return null;
        }
    }
}



