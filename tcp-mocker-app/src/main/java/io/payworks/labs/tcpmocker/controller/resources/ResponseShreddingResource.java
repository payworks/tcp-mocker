package io.payworks.labs.tcpmocker.controller.resources;

public class ResponseShreddingResource {
    private boolean enabled;

    private ResponseShreddingResource() {
    }

    public ResponseShreddingResource(final boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
