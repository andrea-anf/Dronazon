package SmartCity.RPCServices;

import Amministrazione.Coordinates;
import SmartCity.Drone;
import SmartCity.MasterDrone.MasterDronelist;
import grpc.drone.DroneGrpc;
import grpc.drone.DroneOuterClass;
import grpc.drone.DroneOuterClass.AddRequest;
import grpc.drone.DroneOuterClass.AddResponse;
import grpc.drone.DroneOuterClass.Order;
import grpc.drone.DroneOuterClass.OrderAck;
import io.grpc.stub.StreamObserver;

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

        System.out.println("sto ricevendo l'ordine " + order.getId());
        DroneOuterClass.OrderAck ack = DroneOuterClass.OrderAck.newBuilder()
                .setAck(1)
                .build();

        responseObserver.onNext(ack);
        responseObserver.onCompleted();

        //TODO diminuire batteria del drone e mettere sleep per la consegna, quindi comunicare al master statistiche
    }




}
