package SmartCity.RPCServices;

import SmartCity.Drone;
import SmartCity.SmartCity;
import grpc.drone.DroneGrpc;
import grpc.drone.DroneOuterClass.AddRequest;
import grpc.drone.DroneOuterClass.AddResponse;
import grpc.drone.DroneOuterClass.OrderRequest;
import grpc.drone.DroneOuterClass.OrderResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class DroneRPCSendingService extends DroneGrpc.DroneImplBase {

    //looking for master to join the ring?
    public static void addDroneRequest(Drone drone, SmartCity dronelist){
        boolean found = false;
        System.out.println("[+] Looking for the master...");
        for (Drone d : dronelist.getDronelist()){
                final ManagedChannel channel = ManagedChannelBuilder
                        .forTarget(d.getLocalAddress() + ":" + d.getLocalPort())
                        .usePlaintext()
                        .build();

                DroneGrpc.DroneBlockingStub stub = DroneGrpc.newBlockingStub(channel);
                //build RPC request
                AddRequest request = AddRequest.newBuilder()
                        .setId(drone.getId())
                        .setAddress(drone.getLocalAddress())
                        .setPort(drone.getLocalPort())
                        .setCoordX(drone.getCoords().getX())
                        .setCoordY(drone.getCoords().getY())
                        .build();

                //handle response
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
        //if no drones are founds, starts an election
        if(found == false){
            System.out.println("\n[!] No master found");
            System.out.println("[!] Need to starts an election");
        }
    }


    public static int sendOrderRequest(Drone master, Drone drone, String message){
        String[] parts = message.split(";");
        //take id
        String id = parts[0];

        //take departure coordinates
        String[] departureCoords  = parts[1].split(",");
        int depX = Integer.parseInt(departureCoords[0]);
        int depY = Integer.parseInt(departureCoords[1]);

        //take destination coordinates
        String[] destinationCoords  = parts[2].split(",");
        int destX = Integer.parseInt(destinationCoords[0]);
        int destY = Integer.parseInt(destinationCoords[1]);

        final ManagedChannel channel = ManagedChannelBuilder
                .forTarget(drone.getLocalAddress() + ":" + drone.getLocalPort())
                .usePlaintext()
                .build();
        DroneGrpc.DroneBlockingStub stub = DroneGrpc.newBlockingStub(channel);

        OrderRequest request = OrderRequest.newBuilder()
                .setId(id)
                .setDepX(depX)
                .setDepY(depY)
                .setDestX(destX)
                .setDestY(destY)
                .build();

        OrderResponse order = stub.sendOrder(request);
        System.out.println("\n[ORDER] Drone " + drone.getId() + " delivered the order " + id +
                "\n\tArrival Time: " + order.getArrivalTime() +
                "\n\tNew Drone Coords.: (" + order.getNewCoordX() + "," + order.getNewCoordY() + ")" +
                "\n\tKilometers Traveled: " + order.getKmTraveled() +
                "\n\tAir Pollution: " + order.getAirPollution() +
                "\n\tBattery left: " + order.getBatteryLevel() +
                "\n\tCompleted Deliveries: " + order.getDeliveryCompleted() +
                "\n\tDrone is quitting: " + order.getIsQuitting());
        channel.shutdown();

        master.getStats().addToAirPollutionList(order.getAirPollution());
        master.getStats().addToKmTraveledList(order.getKmTraveled());
        master.getStats().addToBatteryLeftList(order.getBatteryLevel());
        if(!drone.isMaster()){
            master.getById(drone.getId()).increseDeliveryCompleted();
        }

            System.out.println("\n[INFO] STATISTICS:" +
                    "\n\tAverage air pollution: " + master.getStats().getAvgAirPollution() +
                    "\n\tAverage km travelled: " + master.getStats().getAvgKmTraveled() +
                    "\n\tAverage battery left: " + master.getStats().getAvgBatteryLeft() +
                    "\n\tAverage delivery completed: " + master.getStats().getAvgDelivery(master.getDronelist()));

        master.getById(drone.getId()).setDelivering(false);

        if(order.getIsQuitting()){
            return drone.getId();
        }
        else{


            return 0;
        }
    }

}
