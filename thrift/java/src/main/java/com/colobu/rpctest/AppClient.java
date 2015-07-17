package com.colobu.rpctest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

public class AppClient {
    public static void main(String[] args) {
        try {
            TTransport transport;
            transport = new TSocket("localhost", 9090);
            transport.open();

            TProtocol protocol = new TBinaryProtocol(transport);
            Greeter.Client client = new Greeter.Client(protocol);

            //warmup
            String name = "world";
            for (int i = 0; i < 100; i++) {
                client.sayHello(name);
            }

            boolean sync = true;
            if (sync) { syncTest(client, name); }
            else { asyncTest(client, name); }


            transport.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void syncTest(Greeter.Client client, String name) throws TException {
        long t = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            client.sayHello(name);
        }
        System.out.println("took: " + (System.nanoTime() - t) / 1000000 + " ms");
    }

    private static void asyncTest(final Greeter.Client client, final String name) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
        final CountDownLatch latch = new CountDownLatch(10000);
        long t = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        client.sayHello(name);
                    }
                    catch (TException e) {
                        e.printStackTrace();
                    }
                    finally {
                        latch.countDown();
                    }
                }
            });

        }

        latch.await();
        System.out.println("took: " + (System.nanoTime() - t) / 1000000 + " ms");
		pool.shutdownNow();
    }
}
