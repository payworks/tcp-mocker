package io.payworks.labs.tcpmocker.controller.model;

public class ResponseShreddingModel {
    private boolean enabled;

    private ResponseShreddingModel() {
    }

    public ResponseShreddingModel(final boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
