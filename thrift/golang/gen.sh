#go get github.com/golang/protobuf/protoc-gen-go
#go get github.com/golang/protobuf/proto
#go get golang.org/x/net/context
#go get google.golang.org/grpc

thrift-0.9.2.exe -r --gen go  -out src thrift/helloworld.thrift