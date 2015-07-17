package com.colobu.rpctest;

import java.util.logging.Logger;
import io.grpc.ServerImpl;
import io.grpc.stub.StreamObserver;
import io.grpc.transport.netty.NettyServerBuilder;

public class AppServer
{
  private static final Logger logger = Logger.getLogger(AppServer.class.getName());

  /* The port on which the server should run */
  private int port = 50051;
  private ServerImpl server;

  private void start() throws Exception {
    server = NettyServerBuilder.forPort(port)
        .addService(GreeterGrpc.bindService(new GreeterImpl()))
        .build().start();
    logger.info("Server started, listening on " + port);
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        System.err.println("*** shutting down gRPC server since JVM is shutting down");
        AppServer.this.stop();
        System.err.println("*** server shut down");
      }
    });
  }

  private void stop() {
    if (server != null) {
      server.shutdown();
    }
  }

  public static void main(String[] args) throws Exception {
    final AppServer server = new AppServer();
    server.start();
  }

  private class GreeterImpl implements GreeterGrpc.Greeter {

    @Override
    public void sayHello(GreeterOuterClass.HelloRequest req, StreamObserver<GreeterOuterClass.HelloReply> responseObserver) {
        GreeterOuterClass.HelloReply reply = GreeterOuterClass.HelloReply.newBuilder().setMessage("Hello " + req.getName()).build();
      responseObserver.onValue(reply);
      responseObserver.onCompleted();
    }
  }
}
