package dronazon;

import Amministrazione.Coordinates;
import org.eclipse.paho.client.mqttv3.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Dronazon {

    public static void main(String[] args) throws InterruptedException {
        Scanner command = new Scanner(System.in);

        MqttClient client;
        String broker = "tcp://localhost:1883";
        String clientId = MqttClient.generateClientId();
        String topic = "dronazon/smartcity/orders/";
        String orderID;


        try {
            client = new MqttClient(broker, clientId);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            // Connect the client
            System.out.println(clientId + " Connecting Broker " + broker);
            client.connect(connOpts);
            System.out.println(clientId + " Connected");

            Runnable sender = new DronazonSender(client, topic, clientId);
            Thread thread = new Thread(sender);
            thread.start();

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

