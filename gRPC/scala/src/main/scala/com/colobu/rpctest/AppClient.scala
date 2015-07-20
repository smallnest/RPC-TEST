package com.colobu.rpctest

import java.util.concurrent.{Executors, CountDownLatch, TimeUnit}
import io.grpc.ChannelImpl
import io.grpc.transport.netty.{NegotiationType, NettyChannelBuilder}

object AppClient extends App{
  val host = "127.0.0.1"
  val port = 50051
  //var  channel:ChannelImpl = NettyChannelBuilder.forAddress(host, port).negotiationType(NegotiationType.PLAINTEXT).build()
  //val  blockingStub:GreeterGrpc.GreeterBlockingStub = GreeterGrpc.newBlockingStub(channel)

  var  channel = new Array[ChannelImpl](20)
  var  blockingStub = new Array[GreeterGrpc.GreeterBlockingStub](20)
  

  def shutdown():Unit = {
    for (i <- 0  to 19) {
      channel(i).shutdown().awaitTerminated(5, TimeUnit.SECONDS)
    }

  }

  def greet(stub:GreeterGrpc.GreeterBlockingStub, name:String):Unit = {
    try {
      //println("Will try to greet " + name + " ...");
      val request = GreeterOuterClass.HelloRequest.newBuilder().setName(name).build()
      val response = stub.sayHello(request)
      //println("Greeting: " + response.getMessage());
    }
    catch  {
      case e:Throwable => println("failed", e)
    }
  }

  def syncTest():Unit = {
    val t = System.nanoTime()
    for (i <- 1 to 10000)
      greet(blockingStub(0),user)
    println("took: " + (System.nanoTime() - t) /1000000 + " ms")
  }

  def asyncTest():Unit = {
    val latch = new CountDownLatch(10000)
    val pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2)
    val t = System.nanoTime()
    for (i <- 1 to 10000)
      pool.submit(new Runnable {
        override def run(): Unit = {greet(blockingStub(i%20), user); latch.countDown()}
      })
    latch.await()
    println("took: " + (System.nanoTime() - t) /1000000 + " ms")
    pool.shutdownNow()
  }

  var user = "world"
  var sync = true
  if (args.length > 0) {
    sync = args(0).toBoolean
  }

  //warmup
  for (i <- 0 to 19) {
    channel(i) = NettyChannelBuilder.forAddress(host, port).negotiationType(NegotiationType.PLAINTEXT).build()
    blockingStub(i) = GreeterGrpc.newBlockingStub(channel(i))
    greet(blockingStub(i), user)
  }

  if (sync)
    syncTest()
  else
    asyncTest

  shutdown()
}
