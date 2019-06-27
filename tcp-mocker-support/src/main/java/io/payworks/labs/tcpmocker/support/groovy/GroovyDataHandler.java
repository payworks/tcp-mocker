package io.payworks.labs.tcpmocker.support.groovy;

import io.payworks.labs.tcpmocker.datahandler.DataHandler;
import io.payworks.labs.tcpmocker.support.DataBuilder;

import java.util.Arrays;
import java.util.Optional;

public abstract class GroovyDataHandler implements DataHandler {

    protected abstract DataBuilder request();
    protected DataBuilder response() {
        return responseBuilder();
    }

    @Override
    public Optional<byte[]> handle(final byte[] data) {
        if (Arrays.equals(request().build(), data)) {
            return Optional.of(response().build());
        } else {
            return Optional.empty();
        }
    }

    protected static DataBuilder requestBuilder() {
        return new DataBuilder();
    }

    protected static DataBuilder responseBuilder() {
        return new DataBuilder();
    }
}
