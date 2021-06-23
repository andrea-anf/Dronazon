package SmartCity;

import Amministrazione.Coordinates;
import grpc.drone.DroneGrpc;
import grpc.drone.DroneOuterClass;
import grpc.drone.DroneOuterClass.AddRequest;
import grpc.drone.DroneOuterClass.AddResponse;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

public class DroneRPCListeningService extends DroneGrpc.DroneImplBase {
    private boolean master;

    public DroneRPCListeningService(boolean isMaster){
        master = isMaster;
    }

    @Override
    public void add(AddRequest request, StreamObserver<DroneOuterClass.AddResponse> responseObserver) {


        AddResponse response;
        if(master){
            //if master, save the new drone and respond with 1
            System.out.println("[+] New drone is joining the ring: " +
                    "\n\tID: " + request.getId() +
                    "\n\tAddress: " + request.getAddress() +
                    "\n\tPort: " + request.getPort() +
                    "\n\tCoords: (" + request.getCoordX() + ", " + request.getCoordY() + ")"
            );

            MasterDronelist list = MasterDronelist.getInstance();
            Drone newDrone = new Drone();

            Coordinates coords = new Coordinates();
            coords.setX(request.getCoordX());
            coords.setY(request.getCoordY());

            newDrone.setCoords(coords);
            list.addDrone(newDrone);

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
