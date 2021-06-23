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
    private boolean master;

    public DroneRPCListeningService(Drone drone){
        master = drone.isMaster();
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

            MasterDronelist masterslist = MasterDronelist.getInstance();
            Drone newDrone = new Drone();

            Coordinates coords = new Coordinates();
            coords.setX(request.getCoordX());
            coords.setY(request.getCoordY());

            newDrone.setCoords(coords);
            masterslist.addDrone(newDrone);

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

    public void takeOrder(Order request, StreamObserver<AddResponse> responseObserver) {
        //TODO diminuire batteria del drone e mettere sleep per la consegna, quindi comunicare al master statistiche
    }

    }
