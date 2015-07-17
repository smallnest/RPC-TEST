package com.colobu.rpctest;

import org.apache.thrift.TException;

public class GreeterHandler implements Greeter.Iface {
    @Override
    public String sayHello(String name) throws TException {
        return "Hello " + name;
    }
}
