package com.colobu.rpctest;

import static io.grpc.stub.Calls.createMethodDescriptor;
import static io.grpc.stub.Calls.asyncUnaryCall;
import static io.grpc.stub.Calls.asyncServerStreamingCall;
import static io.grpc.stub.Calls.asyncClientStreamingCall;
import static io.grpc.stub.Calls.duplexStreamingCall;
import static io.grpc.stub.Calls.blockingUnaryCall;
import static io.grpc.stub.Calls.blockingServerStreamingCall;
import static io.grpc.stub.Calls.unaryFutureCall;
import static io.grpc.stub.ServerCalls.createMethodDefinition;
import static io.grpc.stub.ServerCalls.asyncUnaryRequestCall;
import static io.grpc.stub.ServerCalls.asyncStreamingRequestCall;

@javax.annotation.Generated("by gRPC proto compiler")
public class GreeterGrpc {

  private static final io.grpc.stub.Method<com.colobu.rpctest.GreeterOuterClass.HelloRequest,
      com.colobu.rpctest.GreeterOuterClass.HelloReply> METHOD_SAY_HELLO =
      io.grpc.stub.Method.create(
          io.grpc.MethodType.UNARY, "SayHello",
          io.grpc.protobuf.ProtoUtils.marshaller(com.colobu.rpctest.GreeterOuterClass.HelloRequest.PARSER),
          io.grpc.protobuf.ProtoUtils.marshaller(com.colobu.rpctest.GreeterOuterClass.HelloReply.PARSER));

  public static GreeterStub newStub(io.grpc.Channel channel) {
    return new GreeterStub(channel, CONFIG);
  }

  public static GreeterBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new GreeterBlockingStub(channel, CONFIG);
  }

  public static GreeterFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new GreeterFutureStub(channel, CONFIG);
  }

  public static final GreeterServiceDescriptor CONFIG =
      new GreeterServiceDescriptor();

  @javax.annotation.concurrent.Immutable
  public static class GreeterServiceDescriptor extends
      io.grpc.stub.AbstractServiceDescriptor<GreeterServiceDescriptor> {
    public final io.grpc.MethodDescriptor<com.colobu.rpctest.GreeterOuterClass.HelloRequest,
        com.colobu.rpctest.GreeterOuterClass.HelloReply> sayHello;

    private GreeterServiceDescriptor() {
      sayHello = createMethodDescriptor(
          "greeter.Greeter", METHOD_SAY_HELLO);
    }

    @SuppressWarnings("unchecked")
    private GreeterServiceDescriptor(
        java.util.Map<java.lang.String, io.grpc.MethodDescriptor<?, ?>> methodMap) {
      sayHello = (io.grpc.MethodDescriptor<com.colobu.rpctest.GreeterOuterClass.HelloRequest,
          com.colobu.rpctest.GreeterOuterClass.HelloReply>) methodMap.get(
          CONFIG.sayHello.getName());
    }

    @java.lang.Override
    protected GreeterServiceDescriptor build(
        java.util.Map<java.lang.String, io.grpc.MethodDescriptor<?, ?>> methodMap) {
      return new GreeterServiceDescriptor(methodMap);
    }

    @java.lang.Override
    public com.google.common.collect.ImmutableList<io.grpc.MethodDescriptor<?, ?>> methods() {
      return com.google.common.collect.ImmutableList.<io.grpc.MethodDescriptor<?, ?>>of(
          sayHello);
    }
  }

  public static interface Greeter {

    public void sayHello(com.colobu.rpctest.GreeterOuterClass.HelloRequest request,
        io.grpc.stub.StreamObserver<com.colobu.rpctest.GreeterOuterClass.HelloReply> responseObserver);
  }

  public static interface GreeterBlockingClient {

    public com.colobu.rpctest.GreeterOuterClass.HelloReply sayHello(com.colobu.rpctest.GreeterOuterClass.HelloRequest request);
  }

  public static interface GreeterFutureClient {

    public com.google.common.util.concurrent.ListenableFuture<com.colobu.rpctest.GreeterOuterClass.HelloReply> sayHello(
        com.colobu.rpctest.GreeterOuterClass.HelloRequest request);
  }

  public static class GreeterStub extends
      io.grpc.stub.AbstractStub<GreeterStub, GreeterServiceDescriptor>
      implements Greeter {
    private GreeterStub(io.grpc.Channel channel,
        GreeterServiceDescriptor config) {
      super(channel, config);
    }

    @java.lang.Override
    protected GreeterStub build(io.grpc.Channel channel,
        GreeterServiceDescriptor config) {
      return new GreeterStub(channel, config);
    }

    @java.lang.Override
    public void sayHello(com.colobu.rpctest.GreeterOuterClass.HelloRequest request,
        io.grpc.stub.StreamObserver<com.colobu.rpctest.GreeterOuterClass.HelloReply> responseObserver) {
      asyncUnaryCall(
          channel.newCall(config.sayHello), request, responseObserver);
    }
  }

  public static class GreeterBlockingStub extends
      io.grpc.stub.AbstractStub<GreeterBlockingStub, GreeterServiceDescriptor>
      implements GreeterBlockingClient {
    private GreeterBlockingStub(io.grpc.Channel channel,
        GreeterServiceDescriptor config) {
      super(channel, config);
    }

    @java.lang.Override
    protected GreeterBlockingStub build(io.grpc.Channel channel,
        GreeterServiceDescriptor config) {
      return new GreeterBlockingStub(channel, config);
    }

    @java.lang.Override
    public com.colobu.rpctest.GreeterOuterClass.HelloReply sayHello(com.colobu.rpctest.GreeterOuterClass.HelloRequest request) {
      return blockingUnaryCall(
          channel.newCall(config.sayHello), request);
    }
  }

  public static class GreeterFutureStub extends
      io.grpc.stub.AbstractStub<GreeterFutureStub, GreeterServiceDescriptor>
      implements GreeterFutureClient {
    private GreeterFutureStub(io.grpc.Channel channel,
        GreeterServiceDescriptor config) {
      super(channel, config);
    }

    @java.lang.Override
    protected GreeterFutureStub build(io.grpc.Channel channel,
        GreeterServiceDescriptor config) {
      return new GreeterFutureStub(channel, config);
    }

    @java.lang.Override
    public com.google.common.util.concurrent.ListenableFuture<com.colobu.rpctest.GreeterOuterClass.HelloReply> sayHello(
        com.colobu.rpctest.GreeterOuterClass.HelloRequest request) {
      return unaryFutureCall(
          channel.newCall(config.sayHello), request);
    }
  }

  public static io.grpc.ServerServiceDefinition bindService(
      final Greeter serviceImpl) {
    return io.grpc.ServerServiceDefinition.builder("greeter.Greeter")
      .addMethod(createMethodDefinition(
          METHOD_SAY_HELLO,
          asyncUnaryRequestCall(
            new io.grpc.stub.ServerCalls.UnaryRequestMethod<
                com.colobu.rpctest.GreeterOuterClass.HelloRequest,
                com.colobu.rpctest.GreeterOuterClass.HelloReply>() {
              @java.lang.Override
              public void invoke(
                  com.colobu.rpctest.GreeterOuterClass.HelloRequest request,
                  io.grpc.stub.StreamObserver<com.colobu.rpctest.GreeterOuterClass.HelloReply> responseObserver) {
                serviceImpl.sayHello(request, responseObserver);
              }
            }))).build();
  }
}
