package dronazon;

import Amministrazione.Coordinates;
import SmartCity.MasterDrone.Order;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import javax.swing.table.DefaultTableCellRenderer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ThreadLocalRandom;

public class DronazonSender implements Runnable {
    private MqttClient client;
    private MqttMessage message;
    private String topic;
    private String clientId;
    private int qos = 2;

    public DronazonSender(MqttClient client, String topic, String clientId){
        this.client = client;
        this.topic = topic;
        this.clientId = clientId;
    }
    public void run(){
        while(client.isConnected()){

            String payloadId = new SimpleDateFormat("yyyymmdd_HHmmss")
                    .format(Calendar.getInstance().getTime());

            Coordinates DepartureCoords = new Coordinates(
                    ThreadLocalRandom.current().nextInt(0, 9 + 1),
                    ThreadLocalRandom.current().nextInt(0, 9 + 1));

            Coordinates DestinationCoords = new Coordinates(
                    ThreadLocalRandom.current().nextInt(0, 9 + 1),
                    ThreadLocalRandom.current().nextInt(0, 9 + 1));


//            Order order = new Order();
//            order.setId(payloadId);
//            order.setDeparture(DepartureCoords);
//            order.setDestination(DestinationCoords);
//
            String payloadDepartureCoords = DepartureCoords.getX() +"," + DepartureCoords.getY();
            String payloadDestinationCoords = DestinationCoords.getX() +"," + DestinationCoords.getY();
            String payload = payloadId + ";" + payloadDepartureCoords + ";" + payloadDestinationCoords;

            MqttMessage message = new MqttMessage(payload.getBytes());
            message.setQos(qos);
            System.out.println("\nDronazon is publishing a new delivery: " + message);

            try {
                client.publish(topic, message);
            } catch (MqttException e) {
                e.printStackTrace();
            }
            System.out.println("Delivery published");
            System.out.println("...Press enter to stop the service... ");

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
