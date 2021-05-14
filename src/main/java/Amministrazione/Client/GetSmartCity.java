package Amministrazione.Client;

import Amministrazione.Statistics.StatLists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;


public class GetSmartCity {

    public GetSmartCity(){
        try {

            Client client = Client.create();

            WebResource webResource = client.resource("http://localhost:1338/admin/getSmartCity");
            ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);

            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
            }

            String output = response.getEntity(String.class);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonParser jp = new JsonParser();
            JsonElement je = jp.parse(output);
            String prettyJsonString = gson.toJson(je);

            System.out.println("Output from Server .... \n");
            System.out.println(prettyJsonString);

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

}
