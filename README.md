[![Travis Build Status](https://travis-ci.com/payworks/tcp-mocker.svg?branch=master)](https://travis-ci.com/payworks/tcp-mocker "Travis Build Status")
[![Docker Image Version](https://images.microbadger.com/badges/version/tcpmocker/tcp-mocker-app.svg)](https://microbadger.com/images/tcpmocker/tcp-mocker-app "Docker Image Version")
[![Docker Image Size](https://images.microbadger.com/badges/image/tcpmocker/tcp-mocker-app.svg)](https://microbadger.com/images/tcpmocker/tcp-mocker-app "Docker Image Size")

## tcp-mocker

### Build

##### Maven

`./mvnw clean package`


##### Docker

`docker-compose up`

### Usage

##### Docker:

```
docker run -it --rm \
  -p 10001:10001 \
  -v $(pwd)/tcp-mappings:/var/lib/tcp-mocker/tcp-mappings \
  tcpmocker/tcp-mocker-app:LOCAL-SNAPSHOT
```

##### Maven & Java:

```
<dependency>
    <groupId>io.payworks.labs.tcpmocker</groupId>
    <artifactId>tcp-mocker-service</artifactId>
    <version>LOCAL-SNAPSHOT</version>
</dependency>
```

```
var dataHandlersLoader = new DefaultDataHandlersLoader();
dataHandlersLoader.setMappingsPath("./tcp-mappings")

var serverBuilder = new NettyTcpServerBuilder();
var serverFactory = new TcpServerFactory(serverBuilder, dataHandlersLoader);

serverFactory.createTcpServer(10001);
```


### Tips & Tricks

##### TCP Mocker Ping-Pong Playground

    $ docker run -it --rm \
        -p 10001:10001 \
        -v $(pwd)/tcp-mocker-app-test/tcp-mocker-app/tcp-mappings:/var/lib/tcp-mocker/tcp-mappings \
        tcpmocker/tcp-mocker-app:LOCAL-SNAPSHOT

    $ echo -ne 'ping' | xxd -p
    70696e67
    
    $ echo -ne '\x70\x69\x6e\x67' | xxd -p
    70696e67
    
    $ echo -ne '\x70\x69\x6e\x67' | ncat localhost 10001
    pong

    $ echo -ne '\x70\x69\x6e\x67' | ncat localhost 10001 | xxd -p
    706f6e67

##### Upgrade Maven Wrapper Version

```
mvn -N io.takari:maven:wrapper -Dmaven=3.6.1
```
