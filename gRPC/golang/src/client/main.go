package main

import (
	"fmt"
	"log"
	"os"
	"strconv"
	"sync"
	"time"
	
	pb "greeter"
	
	"golang.org/x/net/context"
	"google.golang.org/grpc"
)

const (
	address     = "localhost:50051"
	defaultName = "world"
)

func invoke(c pb.GreeterClient, name string) {
	r, err := c.SayHello(context.Background(), &pb.HelloRequest{Name: name})
	if err != nil {
		log.Fatalf("could not greet: %v", err)
	}
	_ = r
}

func syncTest(c pb.GreeterClient, name string) {
	i := 10000
	t := time.Now().UnixNano()	
	for ; i>0; i-- {
		invoke(c, name)
	}
	fmt.Println("took", (time.Now().UnixNano() - t) / 1000000, "ms")
}


func asyncTest(c pb.GreeterClient, name string) {
	var wg sync.WaitGroup
    wg.Add(10000)
	
	i := 10000
	t := time.Now().UnixNano()	
	for ; i>0; i-- {
		go func() {invoke(c, name);wg.Done()}()
	}	
	wg.Wait()
	fmt.Println("took", (time.Now().UnixNano() - t) / 1000000, "ms")
}


func main() {
	// Set up a connection to the server.
	conn, err := grpc.Dial(address)
	if err != nil {
		log.Fatalf("did not connect: %v", err)
	}
	defer conn.Close()
	c := pb.NewGreeterClient(conn)

	// Contact the server and print out its response.
	name := defaultName
	sync := true
	if len(os.Args) > 1 {
		sync, err = strconv.ParseBool(os.Args[1])
	}
	
	//warm up
	i := 100
	for ; i>0; i-- {
		invoke(c, name)
	}
	
	if sync {
		syncTest(c, name)
	} else {
		asyncTest(c, name)
	}

	//log.Printf("Greeting: %s", r.Message)
}