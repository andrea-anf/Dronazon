package SmartCity.RPCServices;

import SmartCity.Drone;
import SmartCity.SmartCity;
import grpc.drone.DroneGrpc;
import grpc.drone.DroneOuterClass;
import grpc.drone.DroneOuterClass.AddRequest;
import grpc.drone.DroneOuterClass.AddResponse;
import grpc.drone.DroneOuterClass.OrderRequest;
import grpc.drone.DroneOuterClass.OrderResponse;
import grpc.drone.DroneOuterClass.PingRequest;
import grpc.drone.DroneOuterClass.PingResponse;
import grpc.drone.DroneOuterClass.ElectionReq;
import grpc.drone.DroneOuterClass.ElectionAck;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;

public class DroneRPCSendingService extends DroneGrpc.DroneImplBase {


    public static Drone addDroneRequest(Drone drone, SmartCity dronelist){
        boolean found = false;
        Drone master = new Drone();
        System.out.println("\n[RING] Looking for the master...");

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
                    master = d;

                    drone.setMasterDrone(master);

                    Drone nextD = new Drone();
                    nextD.setId(response.getIDnextDrone());
                    nextD.setLocalAddress(response.getAddressNextDrone());
                    nextD.setLocalPort(response.getPortNextDrone());

                    Drone nextNextD = new Drone();
                    nextNextD.setId(response.getIDnextNextDrone());
                    nextNextD.setLocalAddress(response.getAddressNextNextDrone());
                    nextNextD.setLocalPort(response.getPortNextNextDrone());

                    drone.setNextDrone(nextD);
                    drone.setNextNextDrone(nextNextD);
                    drone.setMasterPrevDrone(response.getIDmasterPrevDrone());

                    System.out.println("[RING] Master found!");

                    if(drone.isMasterPrevDrone()){
                        System.out.println("[RING] Master Previous Drone: True");
                    }
                    drone.showDroneList();
                }
                channel.shutdown();
        }
        //if no drones are founds, starts an election
        if(found == false){
            System.out.println("\n[RING] No master found");
            System.out.println("[RING] Need to starts an election");
            return null;
        }
        else{
            return master;
        }
    }

    public static void sendElection(int starter, Drone target, String message){
        System.out.println("[ELECTION] Sending the election message: message");

        final ManagedChannel channel = ManagedChannelBuilder
                .forTarget(target.getLocalAddress() + ":" + target.getLocalPort())
                .usePlaintext()
                .build();

        DroneGrpc.DroneBlockingStub stub = DroneGrpc.newBlockingStub(channel);

        ElectionReq request = ElectionReq.newBuilder()
                .setMsg(message)
                .setDroneID(starter)
                .build();

        stub.election(request);
        channel.shutdown();
    }

    public static int pingDrone(Drone target, boolean recovery){
        PingResponse response;
        final ManagedChannel channel = ManagedChannelBuilder
                .forTarget(target.getLocalAddress() + ":" + target.getLocalPort())
                .usePlaintext()
                .build();

        DroneGrpc.DroneBlockingStub stub = DroneGrpc.newBlockingStub(channel);

        PingRequest request = PingRequest.newBuilder()
                .setRecovery(recovery)
                .build();

        //handle response
        try{
            response = stub.ping(request);
        }
        catch(StatusRuntimeException error){
            response = null;
        }
        channel.shutdown();

        if(response == null || response.getPingAck()==false){
                return -1;
        }
        else{
            return response.getNextDrone();
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

        channel.shutdown();

        master.getStats().addToAirPollutionList(order.getAirPollution());
        master.getStats().addToKmTraveledList(order.getKmTraveled());
        master.getStats().addToBatteryLeftList(order.getBatteryLevel());
        if(!drone.isMaster()){
            master.getById(drone.getId()).increseDeliveryCompleted();

            System.out.println("\n[ORDER] Drone " + drone.getId() + " delivered the order " + id +
                    "\n\tArrival Time: " + order.getArrivalTime() +
                    "\n\tNew Drone Coords.: (" + order.getNewCoordX() + "," + order.getNewCoordY() + ")" +
                    "\n\tKilometers Traveled: " + order.getKmTraveled() +
                    "\n\tAir Pollution: " + order.getAirPollution() +
                    "\n\tBattery left: " + order.getBatteryLevel() +
                    "\n\tCompleted Deliveries: " + order.getDeliveryCompleted() +
                    "\n\tDrone is quitting: " + order.getIsQuitting());
        }

        master.getById(drone.getId()).setDelivering(false);

        if(order.getIsQuitting()){
            return drone.getId();
        }
        else{

            return 0;
        }
    }

}
