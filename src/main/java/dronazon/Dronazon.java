package dronazon;

import org.eclipse.paho.client.mqttv3.*;

import java.util.Scanner;

public class Dronazon {

    public static void main(String[] args) throws InterruptedException {
        Scanner command = new Scanner(System.in);

        MqttClient client;
        // defining broker and client ID
        String broker = "tcp://localhost:1883";
        String clientId = MqttClient.generateClientId();
        String topic = "dronazon/smartcity/orders/";

        try {
            //create a new client and set options to start a clean session
            client = new MqttClient(broker, clientId);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            // Connect the client
            System.out.println(clientId + " is connecting to Broker " + broker);
            client.connect(connOpts);
            System.out.println(clientId + " Connected");

            //start thread to send orders
            Runnable sender = new DronazonSender(client, topic, clientId);
            Thread thread = new Thread(sender);
            thread.start();

            //wait to disconnect from broker
            if(client.isConnected()){
                command.hasNextLine();
                client.disconnect();
                System.out.println("Dronazon Publisher disconnected");
            }

        } catch (MqttException me ) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }

}

