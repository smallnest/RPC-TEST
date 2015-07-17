package com.colobu.rpctest

import org.apache.thrift.server.TServer
import org.apache.thrift.server.TThreadPoolServer
import org.apache.thrift.transport.TServerSocket
import org.apache.thrift.transport.TServerTransport

object AppServer extends App {
  def simple(processor: Greeter.Processor[GreeterHandler]): Unit = {
    try {
      val serverTransport = new TServerSocket(9090)
      //TServer server = new TSimpleServer(new TServer.Args(serverTransport).processor(processor));

      // Use this for a multithreaded server
      val server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor))

      println("Starting the simple server...")
      server.serve();
    } catch {
      case e: Throwable => e.printStackTrace()
    }
  }

  try {
    val handler = new GreeterHandler()
    val processor = new Greeter.Processor(handler)

    new Thread(new Runnable {
      override def run(): Unit = simple(processor)
    }).start();

  } catch {
    case e: Throwable =>
      e.printStackTrace()
  }
}

class GreeterHandler extends Greeter.Iface{
  override def sayHello(name: String): String = "Hello" + name
}