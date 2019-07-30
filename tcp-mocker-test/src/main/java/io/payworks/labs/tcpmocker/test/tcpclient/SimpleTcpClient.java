package io.payworks.labs.tcpmocker.test.tcpclient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;

public final class SimpleTcpClient implements AutoCloseable {

    private static final int READ_BUF_LEN = 4096;

    private final Socket socket;

    public SimpleTcpClient(final String host, final int port) throws IOException {
        this.socket = new Socket(host, port);
    }

    public byte[] sendAndReceive(final byte[] data) throws IOException {
        send(data);
        return receive(READ_BUF_LEN);
    }

    private void send(final byte[] data) throws IOException {
        socket.getOutputStream().write(data);
    }

    private byte[] receive(final int bufLen) throws IOException {
        final byte[] buf = new byte[bufLen];
        final int len = socket.getInputStream().read(buf);

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        if (len > 0) {
            out.write(buf, 0, len);
        }

        return out.toByteArray();
    }

    @Override
    public void close() throws Exception {
        this.socket.close();
    }
}
