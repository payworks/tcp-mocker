package io.payworks.labs.tcpmocker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MockServerApp {

    public static void main(final String[] args) {
        SpringApplication.run(MockServerApp.class, args);
    }
}
