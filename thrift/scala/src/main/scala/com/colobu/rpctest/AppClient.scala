package com.colobu.rpctest

import java.util.concurrent.{CountDownLatch, Executors}

import org.apache.thrift.protocol.TBinaryProtocol
import org.apache.thrift.transport.TSocket

object AppClient extends App{
  try {
    val transport = new TSocket("localhost", 9090)
    transport.open()

    val protocol = new TBinaryProtocol(transport)
    val client = new Greeter.Client(protocol)

    //warm up
    val name = "world"

    for (i <- 1 to 100) {
      client.sayHello(name)
    }

    var sync = true
    if (args.length > 0) {
      sync = args(0).toBoolean
    }

    if (sync) {
      syncTest(client, name)
    }
    else {
      asyncTest(client, name)
    }


    transport.close()
  }
  catch {
    case e: Throwable => e.printStackTrace()
  }


  def syncTest(client:Greeter.Client , name:String):Unit = {
    val t = System.nanoTime()
    for (i <- 1 to 10000) {
      client.sayHello(name)
    }
    println("took: " + (System.nanoTime() - t) / 1000000 + " ms")
  }

  def asyncTest(client:Greeter.Client , name:String):Unit = {
    val latch = new CountDownLatch(10000)
    val pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2)
    val t = System.nanoTime()
    for (i <- 1 to 10000) {
      pool.submit(new Runnable {
        override def run(): Unit = {client.sayHello(name); latch.countDown()}
      })
    }
    latch.await()
    println("took: " + (System.nanoTime() - t) / 1000000 + " ms")
    pool.shutdownNow()
  }
}
