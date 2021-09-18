package SmartCity.RPCServices;

import SmartCity.Drone;
import SmartCity.MasterDrone.MasterDrone;
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

import java.util.List;
import java.util.concurrent.TimeUnit;

public class DroneRPCSendingService extends DroneGrpc.DroneImplBase {


    public static Drone addDroneRequest(Drone drone){
        boolean masterFound = false;
        Drone master = new Drone();
        System.out.println("\n[RING] Looking for the master...");

        for (Drone d : drone.getDronelist()){
            if(d.getId() == drone.getId())continue;
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
                AddResponse response;

                try{
                    response = stub.add(request);
                }
                catch(StatusRuntimeException sre){
                    response = null;
                }


                if(response != null && response.getResponse() == 1){
                    masterFound = true;
                    master = d;

                    //next drone
                    Drone nextD = new Drone();
                    nextD.setId(response.getIDnextDrone());
                    nextD.setLocalAddress(response.getAddressNextDrone());
                    nextD.setLocalPort(response.getPortNextDrone());

                    //next next drone
                    Drone nextNextD = new Drone();
                    nextNextD.setId(response.getIDnextNextDrone());
                    nextNextD.setLocalAddress(response.getAddressNextNextDrone());
                    nextNextD.setLocalPort(response.getPortNextNextDrone());

                    drone.setNextDrone(nextD);
                    if(response.getIDnextNextDrone() != 0){
                        drone.setNextNextDrone(nextNextD);
                    }
                    drone.setMasterPrevDrone(response.getIDmasterPrevDrone());
                    drone.setMasterDrone(master);

                    System.out.print("[RING] Master found! "+
                            "\n\tMasterDrone: " + drone.getMasterDrone().getId() +
                            "\n\tNextDrone: " + response.getIDnextDrone() +
                            "\n\tNextNextDrone: " + response.getIDnextNextDrone());

                    if(drone.isMasterPrevDrone()){
                        System.out.println("\n\t[RING] Master Previous Drone: True");
                    }
                    drone.showDroneList();
                }
                channel.shutdown();
        }
        //if no drones are founds, starts an election
        if(masterFound == false){
//            if(drone.getDronelist().size() == 1){
//                Runnable sender = new MasterDrone(drone);
//                Thread thread = new Thread(sender);
//                thread.start();
//            }
//            else{
//                DroneRPCSendingService.sendElection(drone.getId(), drone.getBatteryLevel(), drone.getDronelist().get(0));
//            }
            System.out.println("\n[RING] No master found, need to start an election");
            return null;
        }
            return master;

    }

    public static void sendElection(int senderId, int senderBattery, Drone target, String message){
        System.out.println("[ELECTION] Sending the election message to " + target.getId());

        final ManagedChannel channel = ManagedChannelBuilder
                .forTarget(target.getLocalAddress() + ":" + target.getLocalPort())
                .usePlaintext()
                .build();

        DroneGrpc.DroneBlockingStub stub = DroneGrpc.newBlockingStub(channel);
        ElectionReq request = ElectionReq.newBuilder()
                .setMsg(message)
                .setDroneID(senderId)
                .setBatteryLevel(senderBattery)
                .build();

        try{
            ElectionAck resp = stub.election(request);
        }
        catch(StatusRuntimeException sre){
            sre.getStackTrace();
        }
        channel.shutdown();
    }

    public static int pingDrone(Drone target, boolean recovery){
        PingResponse response = null;

        if(target != null) {
            final ManagedChannel channel = ManagedChannelBuilder
                    .forTarget(target.getLocalAddress() + ":" + target.getLocalPort())
                    .usePlaintext()
                    .build();

            DroneGrpc.DroneBlockingStub stub = DroneGrpc.newBlockingStub(channel);

            PingRequest request = PingRequest.newBuilder()
                    .setRecovery(recovery)
                    .build();

            //handle response
            try {
                response = stub.ping(request);
            } catch (StatusRuntimeException error) {
                response = null;
            }
            channel.shutdown();

        }
        if (response == null || response.getPingAck() == false) {
            return -1;
        } else {
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
