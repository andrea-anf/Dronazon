package grpc.drone;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.25.0)",
    comments = "Source: drone.proto")
public final class DroneGrpc {

  private DroneGrpc() {}

  public static final String SERVICE_NAME = "grpc.drone.Drone";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<grpc.drone.DroneOuterClass.AddRequest,
      grpc.drone.DroneOuterClass.AddResponse> getAddMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Add",
      requestType = grpc.drone.DroneOuterClass.AddRequest.class,
      responseType = grpc.drone.DroneOuterClass.AddResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<grpc.drone.DroneOuterClass.AddRequest,
      grpc.drone.DroneOuterClass.AddResponse> getAddMethod() {
    io.grpc.MethodDescriptor<grpc.drone.DroneOuterClass.AddRequest, grpc.drone.DroneOuterClass.AddResponse> getAddMethod;
    if ((getAddMethod = DroneGrpc.getAddMethod) == null) {
      synchronized (DroneGrpc.class) {
        if ((getAddMethod = DroneGrpc.getAddMethod) == null) {
          DroneGrpc.getAddMethod = getAddMethod =
              io.grpc.MethodDescriptor.<grpc.drone.DroneOuterClass.AddRequest, grpc.drone.DroneOuterClass.AddResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Add"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.drone.DroneOuterClass.AddRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.drone.DroneOuterClass.AddResponse.getDefaultInstance()))
              .setSchemaDescriptor(new DroneMethodDescriptorSupplier("Add"))
              .build();
        }
      }
    }
    return getAddMethod;
  }

  private static volatile io.grpc.MethodDescriptor<grpc.drone.DroneOuterClass.Order,
      grpc.drone.DroneOuterClass.OrderAck> getSendOrderMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SendOrder",
      requestType = grpc.drone.DroneOuterClass.Order.class,
      responseType = grpc.drone.DroneOuterClass.OrderAck.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<grpc.drone.DroneOuterClass.Order,
      grpc.drone.DroneOuterClass.OrderAck> getSendOrderMethod() {
    io.grpc.MethodDescriptor<grpc.drone.DroneOuterClass.Order, grpc.drone.DroneOuterClass.OrderAck> getSendOrderMethod;
    if ((getSendOrderMethod = DroneGrpc.getSendOrderMethod) == null) {
      synchronized (DroneGrpc.class) {
        if ((getSendOrderMethod = DroneGrpc.getSendOrderMethod) == null) {
          DroneGrpc.getSendOrderMethod = getSendOrderMethod =
              io.grpc.MethodDescriptor.<grpc.drone.DroneOuterClass.Order, grpc.drone.DroneOuterClass.OrderAck>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SendOrder"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.drone.DroneOuterClass.Order.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.drone.DroneOuterClass.OrderAck.getDefaultInstance()))
              .setSchemaDescriptor(new DroneMethodDescriptorSupplier("SendOrder"))
              .build();
        }
      }
    }
    return getSendOrderMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static DroneStub newStub(io.grpc.Channel channel) {
    return new DroneStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static DroneBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new DroneBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static DroneFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new DroneFutureStub(channel);
  }

  /**
   */
  public static abstract class DroneImplBase implements io.grpc.BindableService {

    /**
     */
    public void add(grpc.drone.DroneOuterClass.AddRequest request,
        io.grpc.stub.StreamObserver<grpc.drone.DroneOuterClass.AddResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getAddMethod(), responseObserver);
    }

    /**
     */
    public void sendOrder(grpc.drone.DroneOuterClass.Order request,
        io.grpc.stub.StreamObserver<grpc.drone.DroneOuterClass.OrderAck> responseObserver) {
      asyncUnimplementedUnaryCall(getSendOrderMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getAddMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                grpc.drone.DroneOuterClass.AddRequest,
                grpc.drone.DroneOuterClass.AddResponse>(
                  this, METHODID_ADD)))
          .addMethod(
            getSendOrderMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                grpc.drone.DroneOuterClass.Order,
                grpc.drone.DroneOuterClass.OrderAck>(
                  this, METHODID_SEND_ORDER)))
          .build();
    }
  }

  /**
   */
  public static final class DroneStub extends io.grpc.stub.AbstractStub<DroneStub> {
    private DroneStub(io.grpc.Channel channel) {
      super(channel);
    }

    private DroneStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DroneStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new DroneStub(channel, callOptions);
    }

    /**
     */
    public void add(grpc.drone.DroneOuterClass.AddRequest request,
        io.grpc.stub.StreamObserver<grpc.drone.DroneOuterClass.AddResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getAddMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void sendOrder(grpc.drone.DroneOuterClass.Order request,
        io.grpc.stub.StreamObserver<grpc.drone.DroneOuterClass.OrderAck> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSendOrderMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class DroneBlockingStub extends io.grpc.stub.AbstractStub<DroneBlockingStub> {
    private DroneBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private DroneBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DroneBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new DroneBlockingStub(channel, callOptions);
    }

    /**
     */
    public grpc.drone.DroneOuterClass.AddResponse add(grpc.drone.DroneOuterClass.AddRequest request) {
      return blockingUnaryCall(
          getChannel(), getAddMethod(), getCallOptions(), request);
    }

    /**
     */
    public grpc.drone.DroneOuterClass.OrderAck sendOrder(grpc.drone.DroneOuterClass.Order request) {
      return blockingUnaryCall(
          getChannel(), getSendOrderMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class DroneFutureStub extends io.grpc.stub.AbstractStub<DroneFutureStub> {
    private DroneFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private DroneFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DroneFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new DroneFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<grpc.drone.DroneOuterClass.AddResponse> add(
        grpc.drone.DroneOuterClass.AddRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getAddMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<grpc.drone.DroneOuterClass.OrderAck> sendOrder(
        grpc.drone.DroneOuterClass.Order request) {
      return futureUnaryCall(
          getChannel().newCall(getSendOrderMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_ADD = 0;
  private static final int METHODID_SEND_ORDER = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final DroneImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(DroneImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_ADD:
          serviceImpl.add((grpc.drone.DroneOuterClass.AddRequest) request,
              (io.grpc.stub.StreamObserver<grpc.drone.DroneOuterClass.AddResponse>) responseObserver);
          break;
        case METHODID_SEND_ORDER:
          serviceImpl.sendOrder((grpc.drone.DroneOuterClass.Order) request,
              (io.grpc.stub.StreamObserver<grpc.drone.DroneOuterClass.OrderAck>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class DroneBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    DroneBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return grpc.drone.DroneOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Drone");
    }
  }

  private static final class DroneFileDescriptorSupplier
      extends DroneBaseDescriptorSupplier {
    DroneFileDescriptorSupplier() {}
  }

  private static final class DroneMethodDescriptorSupplier
      extends DroneBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    DroneMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (DroneGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new DroneFileDescriptorSupplier())
              .addMethod(getAddMethod())
              .addMethod(getSendOrderMethod())
              .build();
        }
      }
    }
    return result;
  }
}
