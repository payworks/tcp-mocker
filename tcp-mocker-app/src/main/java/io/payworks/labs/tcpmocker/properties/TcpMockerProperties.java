package io.payworks.labs.tcpmocker.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Min;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Component
@ConfigurationProperties("tcp-mocker")
public class TcpMockerProperties {

    private int listenPort;
    private String mappingsPath;

    @Min(0)
    @DurationUnit(ChronoUnit.MILLIS)
    private Duration responseDelay = Duration.ZERO;

    private boolean shredResponses = false;

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

    public Duration getResponseDelay() {
        return responseDelay;
    }

    public void setResponseDelay(final Duration responseDelay) {
        this.responseDelay = responseDelay;
    }

    public boolean isShredResponses() {
        return shredResponses;
    }

    public void setShredResponses(final boolean shredResponses) {
        this.shredResponses = shredResponses;
    }
}
