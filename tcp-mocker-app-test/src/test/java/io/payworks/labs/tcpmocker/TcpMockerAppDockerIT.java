package io.payworks.labs.tcpmocker;

import io.payworks.labs.tcpmocker.test.tcpclient.SimpleTcpClient;
import org.testcontainers.containers.DockerComposeContainer;
import org.testng.annotations.*;

import java.io.File;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TcpMockerAppDockerIT {

    private static final String TCP_MOCKER_APP_SERVICE_NAME = "tcp-mocker-app";

    private static final int TCP_SERVICE_PORT = 10001;
    private static final int WEB_SERVICE_PORT = 8080;

    private static final String TCP_MOCKER_APP_CONTAINER_TAG = Optional.ofNullable(System.getenv("PROJECT_VERSION"))
            .orElse("LOCAL-SNAPSHOT");

    private static final DockerComposeContainer tcpMockerContainer =
            new DockerComposeContainer(new File("tcp-mocker-app/docker-compose.yml"))
                    .withExposedService(TCP_MOCKER_APP_SERVICE_NAME, TCP_SERVICE_PORT)
                    .withExposedService(TCP_MOCKER_APP_SERVICE_NAME, WEB_SERVICE_PORT)
                    .withEnv("TCP_MOCKER_APP_TAG", TCP_MOCKER_APP_CONTAINER_TAG)
                    .withLocalCompose(true)
                    .withPull(false);

    private SimpleTcpClient tcpClient;

    @BeforeClass
    public static void startContainer() {
        tcpMockerContainer.start();
    }

    @AfterClass
    public static void stopContainer() {
        tcpMockerContainer.stop();
    }

    @BeforeMethod
    public void setup() throws Exception {
        tcpClient = new SimpleTcpClient(getTcpServiceHost(), getTcpServicePort());
    }

    @AfterMethod
    public void close() throws Exception {
        tcpClient.close();
    }

    @Test
    public void testContainer() throws Exception {
        final String received = new String(tcpClient.sendAndReceive("ping".getBytes(UTF_8)), UTF_8);

        assertThat(received, equalTo("pong"));
    }

    private static Integer getTcpServicePort() {
        return tcpMockerContainer.getServicePort(TCP_MOCKER_APP_SERVICE_NAME, TCP_SERVICE_PORT);
    }

    private static String getTcpServiceHost() {
        return tcpMockerContainer.getServiceHost(TCP_MOCKER_APP_SERVICE_NAME, TCP_SERVICE_PORT);
    }
}
