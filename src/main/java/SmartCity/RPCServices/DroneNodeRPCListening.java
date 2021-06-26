package SmartCity.RPCServices;

import SmartCity.Drone;
import grpc.drone.DroneGrpc;
import grpc.drone.DroneOuterClass;
import grpc.drone.DroneOuterClass.OrderAck;
import grpc.drone.DroneOuterClass.Order;
import io.grpc.stub.StreamObserver;


public class DroneNodeRPCListening extends DroneGrpc.DroneImplBase {
    private Drone drone;

    public DroneNodeRPCListening (Drone drone){
        this.drone = drone;
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
