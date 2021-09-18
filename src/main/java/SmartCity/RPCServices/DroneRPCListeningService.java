package SmartCity.RPCServices;

import Amministrazione.Coordinates;
import SmartCity.CheckDrone;
import SmartCity.Drone;
import SmartCity.MasterDrone.MasterDrone;
import SmartCity.MasterDrone.Orders.DispatchingService;
import SmartCity.SensoreInquinamento.Measurement;
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


import io.grpc.Context;
import io.grpc.stub.StreamObserver;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.sql.Timestamp;

public class DroneRPCListeningService extends DroneGrpc.DroneImplBase {
    private Drone drone;

    public DroneRPCListeningService(Drone drone) {
        this.drone = drone;
    }

    @Override
    public void add(AddRequest request, StreamObserver<AddResponse> responseObserver) {
        AddResponse response;

        Drone newDrone = this.craftNewDrone(request);
        drone.addToDronelist(newDrone);

        System.out.println("\n[RING - " + drone.getId() +"] New drone is joining the ring: " +
                "\n\tID: " + newDrone.getId() +
                "\n\tAddress: " + newDrone.getLocalAddress() +
                "\n\tPort: " + newDrone.getLocalPort() +
                "\n\tCoords: (" + newDrone.getCoords().getX() + ", " + newDrone.getCoords().getY() + ")");


        boolean isPrevToMaster = false;
        if(drone.getDronelist().size() == 2){
            isPrevToMaster = true;
        }


        if (drone.isMaster()) {

            if(drone.getNextNextDrone() != null){
                response = AddResponse.newBuilder()
                        .setResponse(1)

                        .setIDnextDrone(drone.getNextDrone().getId())
                        .setIDnextNextDrone(drone.getNextNextDrone().getId())

                        .setAddressNextDrone(drone.getNextDrone().getLocalAddress())
                        .setAddressNextNextDrone(drone.getNextNextDrone().getLocalAddress())

                        .setPortNextDrone(drone.getNextDrone().getLocalPort())
                        .setPortNextNextDrone(drone.getNextNextDrone().getLocalPort())

                        .setIDmasterPrevDrone(isPrevToMaster)

                        .build();
            }
            else{
                response = AddResponse.newBuilder()
                        .setResponse(1)
                        .setIDnextDrone(drone.getNextDrone().getId())
                        .setIDnextNextDrone(0)

                        .setAddressNextDrone(drone.getNextDrone().getLocalAddress())
                        .setAddressNextNextDrone(drone.getLocalAddress())

                        .setPortNextDrone(drone.getNextDrone().getLocalPort())
                        .setPortNextNextDrone(drone.getLocalPort())
                        .setIDmasterPrevDrone(isPrevToMaster)
                        .build();
            }


            //update pointers to nextDrone and nextNextDrone
            drone.setNextNextDrone(drone.getNextDrone());
            drone.setNextDrone(newDrone);
            drone.showDroneList();

            if(drone.getDronelist().size() <= 2){
                Runnable checkDrone = new CheckDrone(drone);
                Thread threadCheckStatus = new Thread(checkDrone);
                threadCheckStatus.start();
            }

            synchronized (drone.getDronelistLock()) {
                drone.getDronelistLock().notify();
            }
        } else {

            //if this is the masterPrevDrone, it sets as nextNextDrone the new drone
            if (drone.isMasterPrevDrone()) {
                drone.setNextNextDrone(newDrone);
            }
            drone.showDroneList();
            response = AddResponse.newBuilder()
                    .setResponse(0)
                    .build();
        }

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }


    public void ping(PingRequest ping, StreamObserver<PingResponse> responseObserver) {
        int nextDroneID = 0;
        if(ping.getRecovery()){
            nextDroneID = drone.getId();
        }

        boolean quitting = !drone.isQuitting();
        PingResponse pingAck = PingResponse.newBuilder()
                .setPingAck(quitting)
                .setNextDrone(nextDroneID)
                .build();

        responseObserver.onNext(pingAck);
        responseObserver.onCompleted();
    }

    public void election(ElectionReq election, StreamObserver<ElectionAck> responseObserver){

        System.out.println("\n[ELECTION] Received election message" +
                "\n\tMessage: " + election.getMsg() +
                "\n\tCandidate: " + election.getDroneID() +
                "\n\tBatteryLeft: " + election.getBatteryLevel());

        ElectionAck electionAck = ElectionAck.newBuilder().setAck(true).build();

        //check that old master drone has been removed from dronelist
        if(drone.getMasterDrone() != null){
            if(drone.getNextNextDrone().getId() == drone.getMasterDrone().getId()){
                drone.setNextNextDrone(null);
            }
            drone.removeFromDronelist(drone.getMasterDrone());
            drone.setMasterDrone(null);
        }

        if(election.getDroneID() == drone.getId()){
            if(election.getMsg().equals("election")){
                drone.setPartecipation(false);
                System.out.println("\n[ELECTION] Voting terminated, notifying other drones");
                System.out.println("\n[ELECTION] Makes others know who I am");
                DroneRPCSendingService.sendElection(drone.getId(), drone.getBatteryLevel(), drone.getNextDrone(), "elected");
            }
            else{
                System.out.println("\n[ELECTION] Election terminated.");
                drone.showDroneList();
                Runnable sender = new MasterDrone(drone);
                Thread thread = new Thread(sender);
                thread.start();
            }
        }
        else {
            if(election.getMsg().equals("election")){

                //verify battery level
                if(election.getBatteryLevel() > drone.getBatteryLevel()){
                        drone.setPartecipation(true);
                        //if electionID is greater than drone's id, forwards the message
                        System.out.println("[ELECTION] Forwarding the election message:" +
                                "\n\tMessage: " + election.getMsg() +
                                "\n\tCandidate: " + election.getDroneID() +
                                "\n\tBatteryLeft: " + election.getBatteryLevel() +
                                "\n\tto " + drone.getNextDrone().getId());
                        DroneRPCSendingService.sendElection(election.getDroneID(), election.getBatteryLevel(),drone.getNextDrone(), "election");
                }
                else if(election.getBatteryLevel() < drone.getBatteryLevel()){
                    if(drone.isPartecipation() == false){
                        drone.setPartecipation(true);
                        System.out.println("[ELECTION] Updating and forwarding the election message:" +
                                "\n\tMessage: " + election.getMsg() +
                                "\n\tCandidate: " + drone.getId() +
                                "\n\tBatteryLeft: " + drone.getBatteryLevel() +
                                "\n\tTo " + drone.getNextDrone().getId());
                        DroneRPCSendingService.sendElection(drone.getId(), drone.getBatteryLevel(), drone.getNextDrone(), "election");
                    }
                    else{
                        System.out.println("[ELECTION] Replica Election with a lower Id, stopping");
                    }
                }
                //if battery level are the same, pick the one with higher ID
                else{
                    if(election.getDroneID() > drone.getId()){
                        drone.setPartecipation(true);
                        //if electionID is greater than drone's id, forwards the message
                        System.out.println("[ELECTION] Forwarding the election message:" +
                                "\n\tMessage: " + election.getMsg() +
                                "\n\tCandidate: " + election.getDroneID() +
                                "\n\tBatteryLeft: " + election.getBatteryLevel() +
                                "\n\tto " + drone.getNextDrone().getId());
                        DroneRPCSendingService.sendElection(election.getDroneID(),election.getBatteryLevel(),drone.getNextDrone(), "election");
                    }
                    else {
                        if(drone.isPartecipation() == false){
                            drone.setPartecipation(true);
                            System.out.println("[ELECTION] Updating and forwarding the election message:" +
                                    "\n\tMessage: " + election.getMsg() +
                                    "\n\tCandidate: " + drone.getId() +
                                    "\n\tBatteryLeft: " + drone.getBatteryLevel() +
                                    "\n\tTo " + drone.getNextDrone().getId());
                            DroneRPCSendingService.sendElection(drone.getId(), drone.getBatteryLevel(), drone.getNextDrone(), "election");
                        }
                        else{
                            System.out.println("[ELECTION] Replica Election with a lower Id, stopping");
                        }
                    }
                }
            }
            else if(election.getMsg().equals("elected")){
                System.out.println("[ELECTION] forwarding the elected message:" +
                        "\n\tMessage: " + election.getMsg() +
                        "\n\tMaster: " + election.getDroneID() +
                        "\n\tTo " + drone.getNextDrone().getId());

                DroneRPCSendingService.sendElection(election.getDroneID(), election.getBatteryLevel(), drone.getNextDrone(), "elected");
//                drone.getWaitForElectionLock().notify();
                drone.setMasterDrone(drone.getById(election.getDroneID()));
                System.out.println("\n[ELECTION] Election terminated. New Master: " + drone.getMasterDrone().getId());
                drone.showDroneList();
            }
        }
        responseObserver.onNext(electionAck);
        responseObserver.onCompleted();
    }


    @Override
    public void sendOrder(OrderRequest order, StreamObserver<OrderResponse> responseObserver) {
        System.out.println("\n[ORDER] Receiving order number " + order.getId());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\n[ORDER] Order " + order.getId() + " delivered.");

        //set arrival time
        String arrivalTime = new Timestamp(System.currentTimeMillis()).toString();
        System.out.println("\tArrival time: " + arrivalTime);

        //decrease battery level
        drone.setBatteryLevel(drone.getBatteryLevel() - 10);
        //get remaining battery level
        System.out.println("\tBattery left: " + drone.getBatteryLevel());

        DispatchingService dispatchingInfo = new DispatchingService(drone.getDronelist());

        //find distance between initial point and departure point
        Coordinates initialPoint = drone.getCoords();
        double distanceInitialToDepart = dispatchingInfo.Distance(initialPoint.getX(), initialPoint.getY(), order.getDepX(), order.getDestY());

        //set new coordinates
        Coordinates coords = new Coordinates();
        coords.setX(order.getDestX());
        coords.setY(order.getDestY());
        drone.setCoords(coords);
        System.out.println("\tNew coords: (" + drone.getCoords().getX() + "," + drone.getCoords().getY() + ")");

        //find distance between departure point and destination point
        double distanceDepartToDest = dispatchingInfo.Distance(order.getDepX(), order.getDepY(), order.getDestX(), order.getDestY());
        //find kilometers traveled
        double totalDistance = distanceDepartToDest + distanceInitialToDepart;

        drone.setKmTraveled(drone.getKmTraveled() + totalDistance);
        System.out.println("\tKilometers traveled: " + totalDistance);

        //add a new delivery to the total
        drone.increseDeliveryCompleted();
        System.out.println("\tCompleted deliveries: " + drone.getDeliveryCompleted());

        double count = 0;
        for (Measurement val : drone.getBuff().readAllAndClean()) {
            count += val.getValue();
        }
        double airPollution = count / 8;
        System.out.println("\tAvg Pollution: " + airPollution);


        drone.setDelivering(false);

        if (drone.getBatteryLevel() < 15) {
            System.out.println("\n[BATTERY] Low battery level, I need to stop!");
            drone.setQuitting(true);
        }

        DroneOuterClass.OrderResponse stats = DroneOuterClass.OrderResponse.newBuilder()
                .setArrivalTime(arrivalTime)
                .setNewCoordX(drone.getCoords().getX())
                .setNewCoordY(drone.getCoords().getY())
                .setKmTraveled(totalDistance)
                .setAirPollution(airPollution)
                .setBatteryLevel(drone.getBatteryLevel())
                .setIsQuitting(drone.isQuitting())
                .setDeliveryCompleted(drone.getDeliveryCompleted())
                .build();

        synchronized (drone.getDeliveryLock()) {
            drone.getDeliveryLock().notify();
        }

        if (drone.isQuitting() && !drone.isMaster()) {
            try {
                drone.quitDrone();
            } catch (MqttException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        responseObserver.onNext(stats);
        responseObserver.onCompleted();
    }



    Drone craftNewDrone(AddRequest request){
        Drone newDrone = new Drone();

        Coordinates coords = new Coordinates();
        coords.setX(request.getCoordX());
        coords.setY(request.getCoordY());

        newDrone.setId(request.getId());
        newDrone.setLocalAddress(request.getAddress());
        newDrone.setLocalPort(request.getPort());
        newDrone.setCoords(coords);

        return newDrone;
    }
}
