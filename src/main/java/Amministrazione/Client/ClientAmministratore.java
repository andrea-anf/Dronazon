package Amministrazione.Client;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class ClientAmministratore {

    public static void main(String[] args) {
        System.out.println("\n-------------------\n");
        System.out.println("ADMIN CONTROL PANEL");
        System.out.println("\n-------------------\n");


        int operation=0;
        switch (operation){
            case 0: System.out.println("\nscelta operazione 0!");
                    break;
            case 1: System.out.println("\nscelta operazione 1!");
                    break;
            default: System.out.println("\nnada!");
        }


    }
}



