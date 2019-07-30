package io.payworks.labs.tcpmocker.properties;

public class MockServerProperties {

    private int port;
    private String tcpMappingsPath;

    public int getPort() {
        return port;
    }

    public void setPort(final int port) {
        this.port = port;
    }

    public String getTcpMappingsPath() {
        return tcpMappingsPath;
    }

    public void setTcpMappingsPath(final String tcpMappingsPath) {
        this.tcpMappingsPath = tcpMappingsPath;
    }
}
