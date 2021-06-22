package SmartCity;

import grpc.drone.DroneGrpc;
import grpc.drone.DroneOuterClass.AddRequest;
import grpc.drone.DroneOuterClass.AddResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import static grpc.drone.DroneGrpc.newBlockingStub;
public class DroneService extends DroneGrpc.DroneImplBase {

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
}
