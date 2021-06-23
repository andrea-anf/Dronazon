package SmartCity.RPCServices;

import SmartCity.Drone;
import SmartCity.SmartCity;
import grpc.drone.DroneGrpc;
import grpc.drone.DroneOuterClass;
import grpc.drone.DroneOuterClass.AddRequest;
import grpc.drone.DroneOuterClass.AddResponse;
import grpc.drone.DroneOuterClass.Order;
import grpc.drone.DroneOuterClass.OrderAck;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import static grpc.drone.DroneGrpc.newBlockingStub;
public class DroneRPCSendingService extends DroneGrpc.DroneImplBase {

    public static void addRequest(Drone drone, SmartCity dronelist){
        boolean found = false;
        System.out.println("[+] Looking for the master...");
        for (Drone d : dronelist.getDronelist()){
                final ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:"+d.getLocalPort()).usePlaintext().build();

                DroneGrpc.DroneBlockingStub stub = DroneGrpc.newBlockingStub(channel);
                AddRequest request = AddRequest.newBuilder()
                        .setId(drone.getId())
                        .setAddress(drone.getLocalAddress())
                        .setPort(drone.getLocalPort())
                        .setCoordX(drone.getCoords().getX())
                        .setCoordY(drone.getCoords().getY())
                        .build();

                AddResponse response = stub.add(request);
                if(response.getResponse() == 1){
                    found = true;
                    System.out.println("[+] Master found:" +
                            "\n\tID: " + d.getId() +
                            "\n\tAddress: " + d.getLocalAddress() + ":" + d.getLocalPort());
                    drone.setMasterAddress(d.getLocalAddress());
                    drone.setMasterPort(d.getLocalPort());

                }
                channel.shutdown();
        }
        if(found == false){
            System.out.println("\n[!] No master found");
            System.out.println("[!] Need to starts an election");
        }

    }

    public static void sendOrder(Drone drone, String message){
        String[] parts = message.split(";");
        //take id
        int id = Integer.parseInt(parts[0]);

        //take departure coordinates
        String[] departureCoords  = parts[1].split(",");
        int depX = Integer.parseInt(departureCoords[0]);
        int depY = Integer.parseInt(departureCoords[1]);

        //take destination coordinates
        String[] destinationCoords  = parts[2].split(",");
        int destX = Integer.parseInt(destinationCoords[0]);
        int destY = Integer.parseInt(destinationCoords[1]);

        final ManagedChannel channel = ManagedChannelBuilder.forTarget(drone.getLocalAddress() + drone.getLocalPort()).usePlaintext().build();
        DroneGrpc.DroneBlockingStub stub = DroneGrpc.newBlockingStub(channel);

        Order request = Order.newBuilder()
                .setId(id)
                .setDepX(depX)
                .setDepY(depY)
                .setDestX(destX)
                .setDestY(destY)
                .build();

        OrderAck response = stub.sendOrder(request);

    }
}
