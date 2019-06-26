package io.payworks.labs.tcpmocker.support.groovy;

import io.payworks.labs.tcpmocker.datahandler.DataHandler;

import java.util.Optional;

public abstract class GroovyDataHandler implements DataHandler {

    protected abstract boolean matches(byte[] request);

    protected byte[] response() { return new byte[0]; }

    @Override
    public Optional<byte[]> handle(final byte[] data) {
        if (matches(data)) {
            return Optional.of(response());
        } else {
            return Optional.empty();
        }
    }
}
