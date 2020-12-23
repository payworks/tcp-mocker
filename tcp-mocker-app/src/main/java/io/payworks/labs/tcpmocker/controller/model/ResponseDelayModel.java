package io.payworks.labs.tcpmocker.controller.model;

import org.hibernate.validator.constraints.time.DurationMin;

import javax.validation.constraints.NotNull;
import java.time.Duration;

public class ResponseDelayModel {
    @NotNull
    @DurationMin(millis = 0)
    private Duration duration;

    private ResponseDelayModel() {
    }

    public ResponseDelayModel(final Duration duration) {
        this.duration = duration;
    }

    public Duration getDuration() {
        return duration;
    }
}
