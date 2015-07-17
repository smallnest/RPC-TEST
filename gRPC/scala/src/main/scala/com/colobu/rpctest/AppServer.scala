package com.colobu.rpctest

import io.grpc.ServerImpl
import io.grpc.stub.StreamObserver
import io.grpc.transport.netty.NettyServerBuilder

object AppServer extends App {
  val port = 50051
  var server: ServerImpl = _

  def start(): Unit = {
    server = NettyServerBuilder.forPort(port)
      .addService(GreeterGrpc.bindService(new GreeterImpl()))
      .build().start()

    println("Server started, listening on " + port)
    sys.addShutdownHook({
      println("*** shutting down gRPC server since JVM is shutting down")
      stop()
      println("*** server shut down")
    })
  }


  def stop(): Unit = {
    if (server != null) {
      server.shutdown()
    }
  }

  start()

  private class GreeterImpl extends GreeterGrpc.Greeter {
    @Override
    def sayHello(req: GreeterOuterClass.HelloRequest, responseObserver: StreamObserver[GreeterOuterClass.HelloReply]) {
      val reply = GreeterOuterClass.HelloReply.newBuilder().setMessage("Hello " + req.getName()).build()
      responseObserver.onValue(reply)
      responseObserver.onCompleted()
    }
  }

}
