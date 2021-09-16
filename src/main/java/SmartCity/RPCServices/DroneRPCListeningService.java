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
        Drone newDrone = new Drone();

        Coordinates coords = new Coordinates();
        coords.setX(request.getCoordX());
        coords.setY(request.getCoordY());

        newDrone.setId(request.getId());
        newDrone.setLocalAddress(request.getAddress());
        newDrone.setLocalPort(request.getPort());
        newDrone.setCoords(coords);

        //add new drone to the dronelist
        drone.addToDronelist(newDrone);


        System.out.println("\n[RING] New drone is joining the ring: " +
                "\n\tID: " + request.getId() +
                "\n\tAddress: " + request.getAddress() +
                "\n\tPort: " + request.getPort() +
                "\n\tCoords: (" + request.getCoordX() + ", " + request.getCoordY() + ")"
        );
        boolean isPrevToMaster = false;
        if(drone.getDronelist().size() == 2){
            isPrevToMaster = true;
        }


        if (drone.isMaster()) {
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

            //update pointers to nextDrone and nextNextDrone
            drone.setNextNextDrone(drone.getNextDrone());
            drone.setNextDrone(newDrone);

            drone.showDroneList();
            if(drone.getDronelist().size() > 1){
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
            nextDroneID = drone.getNextDrone().getId();
        }

        boolean quitting = !drone.isQuitting();
        PingResponse pingAck = PingResponse.newBuilder()
                .setPingAck(quitting)
                .setNextDrone(nextDroneID)
                .build();

        responseObserver.onNext(pingAck);
        responseObserver.onCompleted();
    }

//    public void election(ElectionReq election, StreamObserver<ElectionAck> responseObserver){
//
//        ElectionAck electionAck = ElectionAck.newBuilder().setAck(true).build();
//
//        System.out.println("\n[ELECTION] Receiving election message" +
//                "\n\tMessage: " + election.getMsg() +
//                "\n\tCandidate: " + election.getDroneID());
//
//        if(election.getDroneID() == drone.getId()){
//            if(election.getMsg().equals("election")){
//                drone.setPartecipation(false);
//                System.out.println("\n[ELECTION] Voting terminated, notifying other drones");
//                System.out.println("\n[ELECTION] Makes others know who I am");
//                if(DroneRPCSendingService.pingDrone(drone.getNextDrone())){
//                    DroneRPCSendingService.sendElection(drone.getId(), drone.getNextDrone(), "elected");
//                }
//                else if(DroneRPCSendingService.pingDrone(drone.getNextNextDrone())){
//                    DroneRPCSendingService.sendElection(drone.getId(), drone.getNextNextDrone(), "elected");
//                }
//                else{
//                    System.out.println("\n[ELECTION] NextDrone and NextNext drone are unreachable.");
//                }
//            }
//            else{
//                System.out.println("\n[ELECTION] Election terminated.");
//
//                Runnable sender = new MasterDrone(drone);
//                Thread thread = new Thread(sender);
//                thread.start();
//            }
//
//        }
//        else {
//            if(election.getMsg().equals("election")){
//                responseToElection(election);
//            }
//            else if(election.getMsg().equals("elected")){
//
//                if(DroneRPCSendingService.pingDrone(drone.getNextDrone())){
//                    DroneRPCSendingService.sendElection(election.getDroneID(), drone.getNextDrone(), "elected");
//                }
//                else if(DroneRPCSendingService.pingDrone(drone.getNextNextDrone())){
//                    DroneRPCSendingService.sendElection(election.getDroneID(), drone.getNextNextDrone(), "elected");
//                }//                drone.setMasterDrone(drone.getById(election.getDroneID()));
//                System.out.println("\n[ELECTION] Election terminated. New Master: " + drone.getMasterDrone().getId());
//            }
//        }
//        responseObserver.onNext(electionAck);
//        responseObserver.onCompleted();
//
//    }


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


//    public void responseToElection(ElectionReq election){
//            if(election.getDroneID() > drone.getId()){
//                drone.setPartecipation(true);
//                //if electionID is greater than drone's id, forwards the message
//                if(DroneRPCSendingService.pingDrone(drone.getNextDrone())){
//                    //send to NextDrone
//                    DroneRPCSendingService.sendElection(election.getDroneID(), drone.getNextDrone(), "election");
//                }
//                else if(DroneRPCSendingService.pingDrone(drone.getNextNextDrone())){
//                    //if NextDrone is unreachable, send to NextNextDrone
//                    DroneRPCSendingService.sendElection(election.getDroneID(), drone.getNextNextDrone(), "election");
//                }
//                else{
//                    System.out.println("[ELECTION] nextDrone and nextNextDrone are unreachable");
//                }
//            }
//            else{
//                if(drone.isPartecipation() == false){
//                    drone.setPartecipation(true);
//
//                    //if electionID is lower than drone's id
//                    if(DroneRPCSendingService.pingDrone(drone.getNextDrone())){
//                        //send to NextDrone
//                        DroneRPCSendingService.sendElection(drone.getId(), drone.getNextDrone(), "election");
//                    }
//                    else if(DroneRPCSendingService.pingDrone(drone.getNextNextDrone())){
//                        //if NextDrone is unreachable, send to NextNextDrone
//                        DroneRPCSendingService.sendElection(drone.getId(), drone.getNextNextDrone(), "election");
//                    }
//                    else{
//                        System.out.println("[ELECTION] nextDrone and nextNextDrone are unreachable");
//                    }
//                }
//                else{
//                    System.out.println("[ELECTION] Replica Election with a lower Id, stopping");
//                }
//            }
//    }

}
