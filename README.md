### Simple Thrift and gRPC performance test

use a simple "helloworld" prototype to test thrift and gRPC.
All servers and clients are implemented by Golang, Java And Scala

Test result as follows (milliseconds/10000 calls). The first value is using one client to test servers and the second value is using 20 clients to test concurrently.

|  | Golang | Java | Scala |
| ----- | ----- | ----- | ----- |
| **Thrift** | 470/231  | 404/381   | 387/355  |
| **gRPC**   | 1375/970 | 4478/4205 | 4733/448 |

