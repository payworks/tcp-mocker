package io.payworks.labs.tcpmocker.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("tcp-mocker")
public class TcpMockerProperties {

    private int listenPort;
    private String mappingsPath;

    public int getListenPort() {
        return listenPort;
    }

    public void setListenPort(final int listenPort) {
        this.listenPort = listenPort;
    }

    public String getMappingsPath() {
        return mappingsPath;
    }

    public void setMappingsPath(final String mappingsPath) {
        this.mappingsPath = mappingsPath;
    }
}
