package dronazon;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class DronazonSender implements Runnable {
    private MqttClient client;
    private MqttMessage message;
    private String topic;
    private String clientId;
    private int qos = 2;

    public DronazonSender(MqttClient client, MqttMessage message, String topic, String clientId){
        this.client = client;
        this.message = message;
        this.topic = topic;
        this.clientId = clientId;
    }
    public void run(){
        while(client.isConnected()){
            message.setQos(qos);
            System.out.println(clientId + " Publishing message: " + message + " ...");

            try {
                client.publish(topic, message);
            } catch (MqttException e) {
                e.printStackTrace();
            }
            System.out.println(clientId + " Message published");

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(Thread.interrupted()){
                break;
            }
        }
    }

}
