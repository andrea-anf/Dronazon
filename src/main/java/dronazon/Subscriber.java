package dronazon;

import org.eclipse.paho.client.mqttv3.*;

import javax.security.auth.callback.Callback;
import java.sql.Timestamp;
import java.util.Scanner;

public class Subscriber {

    public static void main(String[] args) {
        MqttClient client;
        // default MQTT broker address
        String broker = "tcp://localhost:1883";
        String clientId = MqttClient.generateClientId();
        String username = "Susara";
        String password = "123";
        String topic = "dronazon/smartcity/orders/";
        int qos = 2;

        Scanner input = new Scanner(System.in);

        try {
            // Create an Mqtt client
            client = new MqttClient(broker, clientId);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            //setting username and password (OPTIONAL)
            connOpts.setUserName(username);
            connOpts.setPassword(password.toCharArray());


            // Connect the client to the broker (blocking)
            client.connect(connOpts);
            System.out.println(String.format(" %s connected with broker %s", client.getClientId(), client.getServerURI()));

            //handling callback
            client.setCallback(new MqttCallback() {

                public void messageArrived(String topic, MqttMessage message) throws Exception {

                    //tracking of the arrival of the order
                    String time = new Timestamp(System.currentTimeMillis()).toString();
                    String receivedMessage = new String(message.getPayload());

                    System.out.println(clientId +" Received a Message! - Callback - Thread PID: " + Thread.currentThread().getId() +
                            "\n\tTime:    " + time +
                            "\n\tTopic:   " + topic +
                            "\n\tMessage: " + receivedMessage +
                            "\n\tQoS:     " + message.getQos() + "\n");

                    System.out.println("\n ***  Press a random key to exit *** \n");
                }

                public void connectionLost(Throwable cause) {
                    System.out.println(clientId + " Connectionlost! cause:" + cause.getMessage()+ "-  Thread PID: " + Thread.currentThread().getId());
                }

                public void deliveryComplete(IMqttDeliveryToken token) {
                    System.out.println("\n [OK]  Delivery completed\n");

                }
            });

            //subscribing to the defined topic
            System.out.println(clientId + " Subscribing ... - Thread PID: " + Thread.currentThread().getId());
            client.subscribe(topic,qos);
            System.out.println(clientId + " Subscribed to topic : " + topic);


            //waiting input in order to break connection with broker
            System.out.println("\nPress Enter to close connection...");
            input.hasNextLine();
            client.disconnect();
            System.out.println(String.format(" %s disconnected from broker %s", client.getClientId(), client.getServerURI()));


        } catch (MqttException me) {
            // handle exceptions
        }

    }
}
