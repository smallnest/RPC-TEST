package main

import (
	"os"
	"fmt"
	"time"
	"strconv"
	"sync"
	
	"greeter"
	
	"git.apache.org/thrift.git/lib/go/thrift"
	
)

const (
	address     = "localhost:9090"
	defaultName = "world"
)

func syncTest(client *greeter.GreeterClient, name string) {
	i := 10000
	t := time.Now().UnixNano()	
	for ; i>0; i-- {
		client.SayHello(name)
	}
	fmt.Println("took", (time.Now().UnixNano() - t) / 1000000, "ms")
}

func asyncTest(client [20]*greeter.GreeterClient, name string) {
	var locks [20]sync.Mutex
	var wg sync.WaitGroup
    wg.Add(10000)
	
	i := 10000
	t := time.Now().UnixNano()	
	for ; i>0; i-- {
		go func(index int) {
		locks[index % 20].Lock()
		client[ index % 20].SayHello(name)
		wg.Done()
		locks[index % 20].Unlock()
		}(i)
	}	
	wg.Wait()
	fmt.Println("took", (time.Now().UnixNano() - t) / 1000000, "ms")
}


func main() {
	transportFactory := thrift.NewTBufferedTransportFactory(8192)
	protocolFactory := thrift.NewTBinaryProtocolFactoryDefault()

	var client [20]*greeter.GreeterClient
	
	//warm up
	for i := 0; i < 20; i++ {
		transport, err := thrift.NewTSocket(address)
		if err != nil {
			fmt.Fprintln(os.Stderr, "error resolving address:", err)
			os.Exit(1)
		}
		useTransport := transportFactory.GetTransport(transport)
		defer transport.Close()
		
		if err := transport.Open(); err != nil {
			fmt.Fprintln(os.Stderr, "Error opening socket to localhost:9090", " ", err)
			os.Exit(1)
		}
	
		client[i] = greeter.NewGreeterClientFactory(useTransport, protocolFactory)
		client[i].SayHello(defaultName)
	}
	
	sync := true
	if len(os.Args) > 1 {
		sync, _ = strconv.ParseBool(os.Args[1])
	}
	
	if sync {
		syncTest(client[0], defaultName)
	} else {
		asyncTest(client, defaultName)
	}
}