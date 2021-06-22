package SmartCity;

import Amministrazione.Coordinates;
import grpc.drone.DroneGrpc;
import grpc.drone.DroneOuterClass;
import grpc.drone.DroneOuterClass.AddRequest;
import grpc.drone.DroneOuterClass.AddResponse;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

public class MasterDroneService extends DroneGrpc.DroneImplBase {
    private boolean master;
    public MasterDroneService (boolean isMaster){
        master = isMaster;
    }
    @Override
    public void add(AddRequest request, StreamObserver<DroneOuterClass.AddResponse> responseObserver) {


        System.out.println("New drone is joining the ring: " +
                "\nID: " + request.getId() +
                "\nAddress: " + request.getAddress() +
                "\nPort: " + request.getPort() +
                "\nCoordX: " + request.getCoordX() +
                "\nCoordY: " + request.getCoordY()
        );

        MasterDronelist list = MasterDronelist.getInstance();
        Drone newDrone = new Drone();
        Coordinates coords = new Coordinates();
        coords.setX(request.getCoordX());
        coords.setY(request.getCoordY());

        newDrone.setCoords(coords);
        list.addDrone(newDrone);
        AddResponse response;
        if(master){
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
}
