package com.colobu.rpctest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import io.grpc.ChannelImpl;
import io.grpc.transport.netty.NegotiationType;
import io.grpc.transport.netty.NettyChannelBuilder;

public class AppClient {
    private static final Logger logger = Logger.getLogger(AppClient.class.getName());

    private final ChannelImpl channel;
    private final GreeterGrpc.GreeterBlockingStub blockingStub;

    public AppClient(String host, int port) {
        channel = NettyChannelBuilder.forAddress(host, port).negotiationType(NegotiationType.PLAINTEXT).build();
        blockingStub = GreeterGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTerminated(5, TimeUnit.SECONDS);
    }

    public void greet(String name) {
        try {
            //logger.info("Will try to greet " + name + " ...");
            GreeterOuterClass.HelloRequest request = GreeterOuterClass.HelloRequest.newBuilder().setName(name).build();
            GreeterOuterClass.HelloReply response = blockingStub.sayHello(request);
            //logger.info("Greeting: " + response.getMessage());
        }
        catch (RuntimeException e) {
            logger.log(Level.WARNING, "RPC failed", e);
            return;
        }
    }

    public static void syncTest(AppClient client, String user) {
        long t = System.nanoTime();
        for (int i = 0; i < 10000; i++) { client.greet(user); }
        System.out.println("took: " + (System.nanoTime() - t) / 1000000 + " ms");
    }

    public static void asyncTest(final AppClient[] client, final String user) {
        ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
        final CountDownLatch latch = new CountDownLatch(10000);
        long t = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            final int j = i;
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    client[j % 20].greet(user);
                    latch.countDown();
                }
            });
        }
        try {
            latch.await();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("took: " + (System.nanoTime() - t) / 1000000 + " ms");
        pool.shutdownNow();
    }

    public static void main(String[] args) throws Exception {
        AppClient[] client = new AppClient[20];
        try {
            String user = "world";

            int i = 0;
            for (i = 0; i < 20; i++) {
                client[i] = new AppClient("localhost", 50051);
                client[i].greet(user);
            }

            boolean sync = true;
            if (args.length > 0) {
                sync = Boolean.parseBoolean(args[0]);
            }

            if (sync) { syncTest(client[0], user); }
            else { asyncTest(client, user); }
        }
        finally {

            for (int i = 0; i < 20; i++) {
                client[i].shutdown();
            }

        }
    }
}