package SmartCity.RPCServices;

import Amministrazione.Coordinates;
import SmartCity.Drone;
import SmartCity.MasterDrone.Orders.DispatchingService;
import SmartCity.SensoreInquinamento.Measurement;
import grpc.drone.DroneGrpc;
import grpc.drone.DroneOuterClass;
import grpc.drone.DroneOuterClass.AddRequest;
import grpc.drone.DroneOuterClass.AddResponse;
import grpc.drone.DroneOuterClass.OrderRequest;
import grpc.drone.DroneOuterClass.OrderResponse;
import io.grpc.stub.StreamObserver;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.sql.Timestamp;

public class DroneRPCListeningService extends DroneGrpc.DroneImplBase {
    private Drone drone;

    public DroneRPCListeningService(Drone drone){
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

        drone.addToDronelist(newDrone);
        System.out.println("[RING] New drone is joining the ring: " +
                "\n\tID: " + request.getId() +
                "\n\tAddress: " + request.getAddress() +
                "\n\tPort: " + request.getPort() +
                "\n\tCoords: (" + request.getCoordX() + ", " + request.getCoordY() + ")"
        );
        boolean isPrevToMaster = false;
        if(drone.getDronelist().size() == 2){
            isPrevToMaster = true;
        }

        if(drone.isMaster()){
            //if master, save the new drone and respond with 1
             response = AddResponse.newBuilder()
                    .setResponse(1)
                     .setNextDrone(drone.getNextDroneID())
                     .setNextNextDrone(drone.getNextNextDroneID())
                     .setMasterPrevDrone(isPrevToMaster)
                    .build();

             drone.setNextNextDroneID(drone.getNextDroneID());
             drone.setNextDroneID(newDrone.getId());


            synchronized (drone.getDronelistLock()){
                drone.getDronelistLock().notify();
            }
        }
        else{


            if(drone.isMasterPrevDrone()){
                drone.setNextNextDroneID(newDrone.getId());
            }

            response = AddResponse.newBuilder()
                    .setResponse(0)
                    .build();

        }


        responseObserver.onNext(response);
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

        System.out.println("\n[ORDER] Order " + order.getId() + " delivered." );

        //set arrival time
        String arrivalTime = new Timestamp(System.currentTimeMillis()).toString();
        System.out.println("\tArrival time: " + arrivalTime );

        //decrease battery level
        drone.setBatteryLevel(drone.getBatteryLevel()-10);
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
        double totalDistance = distanceDepartToDest+distanceInitialToDepart;

        drone.setKmTraveled(drone.getKmTraveled() + totalDistance);
        System.out.println("\tKilometers traveled: " + totalDistance);

        //add a new delivery to the total
        drone.increseDeliveryCompleted();
        System.out.println("\tCompleted deliveries: " + drone.getDeliveryCompleted());

        double count=0;
        for(Measurement val : drone.getBuff().readAllAndClean()){
            count += val.getValue();
        }
        double airPollution = count/8;
        System.out.println("\tAvg Pollution: " + airPollution );



        drone.setDelivering(false);

        if(drone.getBatteryLevel() < 15) {
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

        synchronized (drone.getDeliveryLock()){
            drone.getDeliveryLock().notify();
        }

        if(drone.isQuitting() && !drone.isMaster()){
            try {
                drone.quitDrone();
            } catch (MqttException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        responseObserver.onNext(stats);
        responseObserver.onCompleted();
    }




}
