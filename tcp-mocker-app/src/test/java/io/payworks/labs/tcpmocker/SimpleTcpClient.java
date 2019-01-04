package io.payworks.labs.tcpmocker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

class SimpleTcpClient implements AutoCloseable {
    private static final int READ_BUF_LEN = 4096;

    private final Socket socket;

    SimpleTcpClient(final String host, final int port) throws IOException {
        this.socket = new Socket(host, port);
    }

    byte[] sendAndReceive(final byte[] data) throws IOException {
        send(data);
        return receive(READ_BUF_LEN);
    }

    private void send(final byte[] data) throws IOException {
        socket.getOutputStream().write(data);
    }

    private byte[] receive(final int bufLen) throws IOException {
        final byte[] buf = new byte[bufLen];
        final int len = socket.getInputStream().read(buf);

        final ByteArrayOutputStream result = new ByteArrayOutputStream();
        if (len > 0) {
            result.write(buf, 0, len);
        }

        return result.toByteArray();
    }

    @Override
    public void close() throws Exception {
        this.socket.close();
    }
}
