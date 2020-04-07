package io.payworks.labs.tcpmocker;

import io.payworks.labs.tcpmocker.datahandler.CompositeDataHandler;
import io.payworks.labs.tcpmocker.datahandler.DataHandler;
import io.payworks.labs.tcpmocker.datahandler.DataHandlerDispatcherFactory;
import io.payworks.labs.tcpmocker.datahandler.LoggingDataHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

import static com.google.common.io.BaseEncoding.base16;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class NettyTcpServerTest {

    private static final Logger logger = LoggerFactory.getLogger(NettyTcpServerTest.class);

    private static final Random RANDOM = new Random();

    private static final int DATA_SIZE = 512;
    private static final int BUFFER_SIZE = 1024;

    private static final String LOOPBACK = "127.0.0.1";

    private static final byte[] TEST_REQUEST = base16().decode("0200703C848020C48092F0F85232050000050009000000000000000500532052124656062918120629090100F1F95232050000050009D18125310000011100000FF7F1C5F9F9F0F0F2F1F5F7F7F7F7F7F7F6404040404040F0F0F2F3F00978F0F0F9F0F0F0F0F0F0F0F200F0F2F9F0F1F9F3F5C195849996898440E385A2A3859940E3A7F0F0F4F4F2F0F9000003");
    private static final byte[] TEST_RESPONSE = base16().decode("0210703C800006C480A2F0F85232050000050009000000000000000500532052124656062918120629F9F2F4F0F0F3F0F0F7F1C5F9F9F0F0F2F1F5F7F7F7F7F7F7F6404040404040F0F0F2F3F00978F0F0F9F0F0F0F0F0F0F0F200F0F0F6F4F4F6F6F6F6000003");

    @Test
    public void testEchoServer() throws Exception {
        try (final NettyTcpServer server = givenNettyTcpServerWithEchoDataHandler();
             final Socket client = new Socket(LOOPBACK, server.getPort())) {
            final byte[] data = new byte[DATA_SIZE];
            RANDOM.nextBytes(data);
            sendReceiveAndVerify(client, data, data);
        }
    }

    private NettyTcpServer givenNettyTcpServerWithEchoDataHandler() {
        return givenNettyTcpServerBuilderWith(new EchoDataHandler()).build();
    }

    private NettyTcpServerBuilder givenNettyTcpServerBuilderWith(final DataHandler dataHandler) {
        final NettyTcpServerBuilder serverBuilder =
                new NettyTcpServerBuilder()
                        .withDataHandlerDispatcherFactory(givenDataHandlerDispatcherFactory());

        serverBuilder.withDataHandler(dataHandler);
        serverBuilder.withPort(0);
        return serverBuilder;
    }

    @Test
    public void testRequestResponseServer() throws Exception {
        try (final NettyTcpServer server = givenNettyTcpServerWithTestRequestDataHandler();
             final Socket client = new Socket(LOOPBACK, server.getPort())) {
            sendReceiveAndVerify(client, TEST_REQUEST, TEST_RESPONSE);
        }
    }

    private NettyTcpServer givenNettyTcpServerWithTestRequestDataHandler() {
        return givenNettyTcpServerBuilderWith(new TestRequestDataHandler()).build();
    }

    private void sendReceiveAndVerify(final Socket client, final byte[] data, final byte[] expected) throws IOException {
        final byte[] buffer = sendAndReceive(client, data);
        assertThat(Arrays.copyOf(buffer, expected.length), equalTo(expected));
    }

    private byte[] sendAndReceive(final Socket client, final byte[] data) throws IOException {
        final byte[] buffer;
        try (final InputStream clientInputStream = client.getInputStream();
             final OutputStream clientOutputStream = client.getOutputStream()) {
            clientOutputStream.write(data);

            buffer = new byte[BUFFER_SIZE];
            final int received = clientInputStream.read(buffer);

            logger.trace("Sent: {}", data);
            logger.trace("Received ({}): {}", received, buffer);
        }
        return buffer;
    }

    private static final class EchoDataHandler implements DataHandler {
        @Override
        public Optional<byte[]> handle(final byte[] data) {
            return Optional.of(data);
        }
    }

    private static final class TestRequestDataHandler implements DataHandler {
        @Override
        public Optional<byte[]> handle(final byte[] data) {
            if (Arrays.equals(TEST_REQUEST, Arrays.copyOf(data, TEST_REQUEST.length))) {
                return Optional.of(TEST_RESPONSE);
            } else {
                return Optional.empty();
            }
        }
    }

    private DataHandlerDispatcherFactory givenDataHandlerDispatcherFactory() {
        return collection ->
                new LoggingDataHandler(
                        new CompositeDataHandler(collection));
    }

    @Test
    public void testCloseServer() throws Exception {
        try (final NettyTcpServer server = givenNettyTcpServerWithTestRequestDataHandler();
             final Socket client = new Socket(LOOPBACK, server.getPort())) {

            await().until(client::isConnected);

            server.close();

            await().until(() -> sendAndReceive(client, TEST_REQUEST), λ -> client.isClosed());
        }
    }
}