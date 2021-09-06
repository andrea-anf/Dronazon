package SmartCity.RPCServices;

import Amministrazione.Coordinates;
import SmartCity.Drone;
import SmartCity.MasterDrone.DispatchingService;
import grpc.drone.DroneGrpc;
import grpc.drone.DroneOuterClass;
import grpc.drone.DroneOuterClass.AddRequest;
import grpc.drone.DroneOuterClass.AddResponse;
import grpc.drone.DroneOuterClass.Order;
import grpc.drone.DroneOuterClass.OrderAck;
import io.grpc.stub.StreamObserver;

import java.sql.Timestamp;

public class DroneRPCListeningService extends DroneGrpc.DroneImplBase {
    private Drone drone;

    public DroneRPCListeningService(Drone drone){
        this.drone = drone;
    }

    @Override
    public void add(AddRequest request, StreamObserver<AddResponse> responseObserver) {
        AddResponse response;
        if(drone.isMaster()){
            //if master, save the new drone and respond with 1
            System.out.println("[+] New drone is joining the ring: " +
                    "\n\tID: " + request.getId() +
                    "\n\tAddress: " + request.getAddress() +
                    "\n\tPort: " + request.getPort() +
                    "\n\tCoords: (" + request.getCoordX() + ", " + request.getCoordY() + ")"
            );
            Drone newDrone = new Drone();

            Coordinates coords = new Coordinates();
            coords.setX(request.getCoordX());
            coords.setY(request.getCoordY());

            newDrone.setId(request.getId());
            newDrone.setLocalAddress(request.getAddress());
            newDrone.setLocalPort(request.getPort());
            newDrone.setCoords(coords);

            drone.addToDronelist(newDrone);

             response = AddResponse.newBuilder()
                    .setResponse(1)
                    .build();

        }
        else{
            response = AddResponse.newBuilder()
                    .setResponse(0)
                    .build();
        }

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void sendOrder(Order order, StreamObserver<OrderAck> responseObserver) {
        drone.setDelivering(true);
        System.out.println("Receiving order number " + order.getId());
        DroneOuterClass.OrderAck ack = DroneOuterClass.OrderAck.newBuilder()
                .setAck(1)
                .build();


        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        drone.setDelivering(false);
        System.out.println("[+] Order " + order.getId() + " delivered." );

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

        //fin distance between departure point and destination point

        //set new coordinates
        Coordinates coords = new Coordinates();
        coords.setX(order.getDestX());
        coords.setY(order.getDestY());
        drone.setCoords(coords);
        System.out.println("\tNew coords: (" + drone.getCoords().getX() + "," + drone.getCoords().getY() + ")");


        //find kilometers traveled
        double distanceDepartToDest = dispatchingInfo.Distance(order.getDepX(), order.getDepY(), order.getDestX(), order.getDestY());
        System.out.println("\tKilometers traveled: " + distanceDepartToDest+distanceInitialToDepart);

        if(drone.getBatteryLevel() < 15) {
            System.out.println("Low battery level, I need to stop! ");
        }
        responseObserver.onNext(ack);
        responseObserver.onCompleted();

    }




}
