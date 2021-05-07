package dronazon;

import org.eclipse.paho.client.mqttv3.*;

import java.util.Random;
import java.util.Scanner;

public class Dronazon {

    public static void main(String[] args) throws InterruptedException {
        Scanner command = new Scanner(System.in);

        MqttClient client;
        String broker = "tcp://localhost:1883";
        String clientId = MqttClient.generateClientId();
        String topic = "dronazon/smartcity/orders/";
        String orderID;
        int coordX=0;
        int coordY=0;
        int maxCoord = 9;
        int minCoord = 0;

        int qos = 2;

        // valori generazione ID ordine
        int max = 200;
        int min = 100;


        try {
            client = new MqttClient(broker, clientId);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            // Connect the client
            System.out.println(clientId + " Connecting Broker " + broker);
            client.connect(connOpts);
            System.out.println(clientId + " Connected");

            orderID = String.valueOf(Math.floor(Math.random()*(max-min+1)+max));
            coordX = (int)Math.floor(Math.random()*(maxCoord-minCoord+1)+maxCoord);
            coordY = (int)Math.floor(Math.random()*(maxCoord-minCoord+1)+maxCoord);

//            String payload = "dafare"; //ID, Punto ritiro, punto consegna
//            MqttMessage message = new MqttMessage(payload.getBytes());
//
//            Runnable sender = new DronazonSender(client, message, topic, clientId);
//
//            Thread thread = new Thread(sender);
//            thread.start();
              // Set the QoS on the Message

            if(client.isConnected()){
                command.hasNextLine();
                client.disconnect();
                System.out.println("Publisher " + clientId + " disconnected");
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

