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

  private static volatile io.grpc.MethodDescriptor<grpc.drone.DroneOuterClass.OrderRequest,
      grpc.drone.DroneOuterClass.OrderResponse> getSendOrderMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SendOrder",
      requestType = grpc.drone.DroneOuterClass.OrderRequest.class,
      responseType = grpc.drone.DroneOuterClass.OrderResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<grpc.drone.DroneOuterClass.OrderRequest,
      grpc.drone.DroneOuterClass.OrderResponse> getSendOrderMethod() {
    io.grpc.MethodDescriptor<grpc.drone.DroneOuterClass.OrderRequest, grpc.drone.DroneOuterClass.OrderResponse> getSendOrderMethod;
    if ((getSendOrderMethod = DroneGrpc.getSendOrderMethod) == null) {
      synchronized (DroneGrpc.class) {
        if ((getSendOrderMethod = DroneGrpc.getSendOrderMethod) == null) {
          DroneGrpc.getSendOrderMethod = getSendOrderMethod =
              io.grpc.MethodDescriptor.<grpc.drone.DroneOuterClass.OrderRequest, grpc.drone.DroneOuterClass.OrderResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SendOrder"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.drone.DroneOuterClass.OrderRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.drone.DroneOuterClass.OrderResponse.getDefaultInstance()))
              .setSchemaDescriptor(new DroneMethodDescriptorSupplier("SendOrder"))
              .build();
        }
      }
    }
    return getSendOrderMethod;
  }

  private static volatile io.grpc.MethodDescriptor<grpc.drone.DroneOuterClass.PingRequest,
      grpc.drone.DroneOuterClass.PingResponse> getPingMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Ping",
      requestType = grpc.drone.DroneOuterClass.PingRequest.class,
      responseType = grpc.drone.DroneOuterClass.PingResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<grpc.drone.DroneOuterClass.PingRequest,
      grpc.drone.DroneOuterClass.PingResponse> getPingMethod() {
    io.grpc.MethodDescriptor<grpc.drone.DroneOuterClass.PingRequest, grpc.drone.DroneOuterClass.PingResponse> getPingMethod;
    if ((getPingMethod = DroneGrpc.getPingMethod) == null) {
      synchronized (DroneGrpc.class) {
        if ((getPingMethod = DroneGrpc.getPingMethod) == null) {
          DroneGrpc.getPingMethod = getPingMethod =
              io.grpc.MethodDescriptor.<grpc.drone.DroneOuterClass.PingRequest, grpc.drone.DroneOuterClass.PingResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Ping"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.drone.DroneOuterClass.PingRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.drone.DroneOuterClass.PingResponse.getDefaultInstance()))
              .setSchemaDescriptor(new DroneMethodDescriptorSupplier("Ping"))
              .build();
        }
      }
    }
    return getPingMethod;
  }

  private static volatile io.grpc.MethodDescriptor<grpc.drone.DroneOuterClass.ElectionReq,
      grpc.drone.DroneOuterClass.ElectionAck> getElectionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Election",
      requestType = grpc.drone.DroneOuterClass.ElectionReq.class,
      responseType = grpc.drone.DroneOuterClass.ElectionAck.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<grpc.drone.DroneOuterClass.ElectionReq,
      grpc.drone.DroneOuterClass.ElectionAck> getElectionMethod() {
    io.grpc.MethodDescriptor<grpc.drone.DroneOuterClass.ElectionReq, grpc.drone.DroneOuterClass.ElectionAck> getElectionMethod;
    if ((getElectionMethod = DroneGrpc.getElectionMethod) == null) {
      synchronized (DroneGrpc.class) {
        if ((getElectionMethod = DroneGrpc.getElectionMethod) == null) {
          DroneGrpc.getElectionMethod = getElectionMethod =
              io.grpc.MethodDescriptor.<grpc.drone.DroneOuterClass.ElectionReq, grpc.drone.DroneOuterClass.ElectionAck>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Election"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.drone.DroneOuterClass.ElectionReq.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.drone.DroneOuterClass.ElectionAck.getDefaultInstance()))
              .setSchemaDescriptor(new DroneMethodDescriptorSupplier("Election"))
              .build();
        }
      }
    }
    return getElectionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<grpc.drone.DroneOuterClass.RechargeRequest,
      grpc.drone.DroneOuterClass.RechargeResponse> getRechargeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Recharge",
      requestType = grpc.drone.DroneOuterClass.RechargeRequest.class,
      responseType = grpc.drone.DroneOuterClass.RechargeResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<grpc.drone.DroneOuterClass.RechargeRequest,
      grpc.drone.DroneOuterClass.RechargeResponse> getRechargeMethod() {
    io.grpc.MethodDescriptor<grpc.drone.DroneOuterClass.RechargeRequest, grpc.drone.DroneOuterClass.RechargeResponse> getRechargeMethod;
    if ((getRechargeMethod = DroneGrpc.getRechargeMethod) == null) {
      synchronized (DroneGrpc.class) {
        if ((getRechargeMethod = DroneGrpc.getRechargeMethod) == null) {
          DroneGrpc.getRechargeMethod = getRechargeMethod =
              io.grpc.MethodDescriptor.<grpc.drone.DroneOuterClass.RechargeRequest, grpc.drone.DroneOuterClass.RechargeResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Recharge"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.drone.DroneOuterClass.RechargeRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.drone.DroneOuterClass.RechargeResponse.getDefaultInstance()))
              .setSchemaDescriptor(new DroneMethodDescriptorSupplier("Recharge"))
              .build();
        }
      }
    }
    return getRechargeMethod;
  }

  private static volatile io.grpc.MethodDescriptor<grpc.drone.DroneOuterClass.RechargePermission,
      grpc.drone.DroneOuterClass.RechargePermissionAck> getRechargeOKMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RechargeOK",
      requestType = grpc.drone.DroneOuterClass.RechargePermission.class,
      responseType = grpc.drone.DroneOuterClass.RechargePermissionAck.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<grpc.drone.DroneOuterClass.RechargePermission,
      grpc.drone.DroneOuterClass.RechargePermissionAck> getRechargeOKMethod() {
    io.grpc.MethodDescriptor<grpc.drone.DroneOuterClass.RechargePermission, grpc.drone.DroneOuterClass.RechargePermissionAck> getRechargeOKMethod;
    if ((getRechargeOKMethod = DroneGrpc.getRechargeOKMethod) == null) {
      synchronized (DroneGrpc.class) {
        if ((getRechargeOKMethod = DroneGrpc.getRechargeOKMethod) == null) {
          DroneGrpc.getRechargeOKMethod = getRechargeOKMethod =
              io.grpc.MethodDescriptor.<grpc.drone.DroneOuterClass.RechargePermission, grpc.drone.DroneOuterClass.RechargePermissionAck>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "RechargeOK"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.drone.DroneOuterClass.RechargePermission.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.drone.DroneOuterClass.RechargePermissionAck.getDefaultInstance()))
              .setSchemaDescriptor(new DroneMethodDescriptorSupplier("RechargeOK"))
              .build();
        }
      }
    }
    return getRechargeOKMethod;
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
    public void sendOrder(grpc.drone.DroneOuterClass.OrderRequest request,
        io.grpc.stub.StreamObserver<grpc.drone.DroneOuterClass.OrderResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getSendOrderMethod(), responseObserver);
    }

    /**
     */
    public void ping(grpc.drone.DroneOuterClass.PingRequest request,
        io.grpc.stub.StreamObserver<grpc.drone.DroneOuterClass.PingResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getPingMethod(), responseObserver);
    }

    /**
     */
    public void election(grpc.drone.DroneOuterClass.ElectionReq request,
        io.grpc.stub.StreamObserver<grpc.drone.DroneOuterClass.ElectionAck> responseObserver) {
      asyncUnimplementedUnaryCall(getElectionMethod(), responseObserver);
    }

    /**
     */
    public void recharge(grpc.drone.DroneOuterClass.RechargeRequest request,
        io.grpc.stub.StreamObserver<grpc.drone.DroneOuterClass.RechargeResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getRechargeMethod(), responseObserver);
    }

    /**
     */
    public void rechargeOK(grpc.drone.DroneOuterClass.RechargePermission request,
        io.grpc.stub.StreamObserver<grpc.drone.DroneOuterClass.RechargePermissionAck> responseObserver) {
      asyncUnimplementedUnaryCall(getRechargeOKMethod(), responseObserver);
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
                grpc.drone.DroneOuterClass.OrderRequest,
                grpc.drone.DroneOuterClass.OrderResponse>(
                  this, METHODID_SEND_ORDER)))
          .addMethod(
            getPingMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                grpc.drone.DroneOuterClass.PingRequest,
                grpc.drone.DroneOuterClass.PingResponse>(
                  this, METHODID_PING)))
          .addMethod(
            getElectionMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                grpc.drone.DroneOuterClass.ElectionReq,
                grpc.drone.DroneOuterClass.ElectionAck>(
                  this, METHODID_ELECTION)))
          .addMethod(
            getRechargeMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                grpc.drone.DroneOuterClass.RechargeRequest,
                grpc.drone.DroneOuterClass.RechargeResponse>(
                  this, METHODID_RECHARGE)))
          .addMethod(
            getRechargeOKMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                grpc.drone.DroneOuterClass.RechargePermission,
                grpc.drone.DroneOuterClass.RechargePermissionAck>(
                  this, METHODID_RECHARGE_OK)))
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
    public void sendOrder(grpc.drone.DroneOuterClass.OrderRequest request,
        io.grpc.stub.StreamObserver<grpc.drone.DroneOuterClass.OrderResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSendOrderMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void ping(grpc.drone.DroneOuterClass.PingRequest request,
        io.grpc.stub.StreamObserver<grpc.drone.DroneOuterClass.PingResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getPingMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void election(grpc.drone.DroneOuterClass.ElectionReq request,
        io.grpc.stub.StreamObserver<grpc.drone.DroneOuterClass.ElectionAck> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getElectionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void recharge(grpc.drone.DroneOuterClass.RechargeRequest request,
        io.grpc.stub.StreamObserver<grpc.drone.DroneOuterClass.RechargeResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRechargeMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void rechargeOK(grpc.drone.DroneOuterClass.RechargePermission request,
        io.grpc.stub.StreamObserver<grpc.drone.DroneOuterClass.RechargePermissionAck> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRechargeOKMethod(), getCallOptions()), request, responseObserver);
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
    public grpc.drone.DroneOuterClass.OrderResponse sendOrder(grpc.drone.DroneOuterClass.OrderRequest request) {
      return blockingUnaryCall(
          getChannel(), getSendOrderMethod(), getCallOptions(), request);
    }

    /**
     */
    public grpc.drone.DroneOuterClass.PingResponse ping(grpc.drone.DroneOuterClass.PingRequest request) {
      return blockingUnaryCall(
          getChannel(), getPingMethod(), getCallOptions(), request);
    }

    /**
     */
    public grpc.drone.DroneOuterClass.ElectionAck election(grpc.drone.DroneOuterClass.ElectionReq request) {
      return blockingUnaryCall(
          getChannel(), getElectionMethod(), getCallOptions(), request);
    }

    /**
     */
    public grpc.drone.DroneOuterClass.RechargeResponse recharge(grpc.drone.DroneOuterClass.RechargeRequest request) {
      return blockingUnaryCall(
          getChannel(), getRechargeMethod(), getCallOptions(), request);
    }

    /**
     */
    public grpc.drone.DroneOuterClass.RechargePermissionAck rechargeOK(grpc.drone.DroneOuterClass.RechargePermission request) {
      return blockingUnaryCall(
          getChannel(), getRechargeOKMethod(), getCallOptions(), request);
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
    public com.google.common.util.concurrent.ListenableFuture<grpc.drone.DroneOuterClass.OrderResponse> sendOrder(
        grpc.drone.DroneOuterClass.OrderRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSendOrderMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<grpc.drone.DroneOuterClass.PingResponse> ping(
        grpc.drone.DroneOuterClass.PingRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getPingMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<grpc.drone.DroneOuterClass.ElectionAck> election(
        grpc.drone.DroneOuterClass.ElectionReq request) {
      return futureUnaryCall(
          getChannel().newCall(getElectionMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<grpc.drone.DroneOuterClass.RechargeResponse> recharge(
        grpc.drone.DroneOuterClass.RechargeRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getRechargeMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<grpc.drone.DroneOuterClass.RechargePermissionAck> rechargeOK(
        grpc.drone.DroneOuterClass.RechargePermission request) {
      return futureUnaryCall(
          getChannel().newCall(getRechargeOKMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_ADD = 0;
  private static final int METHODID_SEND_ORDER = 1;
  private static final int METHODID_PING = 2;
  private static final int METHODID_ELECTION = 3;
  private static final int METHODID_RECHARGE = 4;
  private static final int METHODID_RECHARGE_OK = 5;

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
          serviceImpl.sendOrder((grpc.drone.DroneOuterClass.OrderRequest) request,
              (io.grpc.stub.StreamObserver<grpc.drone.DroneOuterClass.OrderResponse>) responseObserver);
          break;
        case METHODID_PING:
          serviceImpl.ping((grpc.drone.DroneOuterClass.PingRequest) request,
              (io.grpc.stub.StreamObserver<grpc.drone.DroneOuterClass.PingResponse>) responseObserver);
          break;
        case METHODID_ELECTION:
          serviceImpl.election((grpc.drone.DroneOuterClass.ElectionReq) request,
              (io.grpc.stub.StreamObserver<grpc.drone.DroneOuterClass.ElectionAck>) responseObserver);
          break;
        case METHODID_RECHARGE:
          serviceImpl.recharge((grpc.drone.DroneOuterClass.RechargeRequest) request,
              (io.grpc.stub.StreamObserver<grpc.drone.DroneOuterClass.RechargeResponse>) responseObserver);
          break;
        case METHODID_RECHARGE_OK:
          serviceImpl.rechargeOK((grpc.drone.DroneOuterClass.RechargePermission) request,
              (io.grpc.stub.StreamObserver<grpc.drone.DroneOuterClass.RechargePermissionAck>) responseObserver);
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
              .addMethod(getPingMethod())
              .addMethod(getElectionMethod())
              .addMethod(getRechargeMethod())
              .addMethod(getRechargeOKMethod())
              .build();
        }
      }
    }
    return result;
  }
}
