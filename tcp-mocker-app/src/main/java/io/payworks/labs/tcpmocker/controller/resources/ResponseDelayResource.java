package io.payworks.labs.tcpmocker.controller.resources;

import org.hibernate.validator.constraints.time.DurationMin;

import javax.validation.constraints.NotNull;
import java.time.Duration;

public class ResponseDelayResource {
    @NotNull
    @DurationMin(millis = 0)
    private Duration duration;

    private ResponseDelayResource() {
    }

    public ResponseDelayResource(Duration duration) {
        this.duration = duration;
    }

    public Duration getDuration() {
        return duration;
    }
}
