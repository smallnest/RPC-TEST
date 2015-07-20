package main

import (
    "fmt"
	"os"
	
    "git.apache.org/thrift.git/lib/go/thrift"
    
	"greeter"
)

const (
	NetworkAddr = "localhost:9090"
)


type GreeterHandler struct {
	
}

func NewGreeterHandler() *GreeterHandler {
	return &GreeterHandler{}
}

func (p *GreeterHandler) SayHello(name string)(r string, err error) { 
	return "Hello " + name, nil
}

func main() {
	var protocolFactory thrift.TProtocolFactory = thrift.NewTBinaryProtocolFactoryDefault()
	var transportFactory thrift.TTransportFactory = thrift.NewTBufferedTransportFactory(8192)
	transport, err := thrift.NewTServerSocket(NetworkAddr)
	if err != nil {
		fmt.Println("Error!", err)
		os.Exit(1)
	}

    handler := NewGreeterHandler()
    processor := greeter.NewGreeterProcessor(handler)
    server := thrift.NewTSimpleServer4(processor, transport, transportFactory, protocolFactory)
	fmt.Println("Starting the simple server... on ", NetworkAddr)
	server.Serve()
}