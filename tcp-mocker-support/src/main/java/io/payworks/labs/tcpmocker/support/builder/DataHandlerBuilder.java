package io.payworks.labs.tcpmocker.support.builder;

import io.payworks.labs.tcpmocker.datahandler.DataHandler;

import java.util.Arrays;
import java.util.Optional;

public class DataHandlerBuilder {

    private BinaryBuilder requestBuilder;
    private BinaryBuilder responseBuilder = new BinaryBuilder();

    public DataHandlerBuilder request(final BinaryBuilder requestBuilder) {
        this.requestBuilder = requestBuilder;
        return this;
    }

    public DataHandlerBuilder response(final BinaryBuilder responseBuilder) {
        this.responseBuilder = responseBuilder;
        return this;
    }

    public DataHandler build() {
        return data -> {
            if (Arrays.equals(requestBuilder.build(), data)) {
                return Optional.of(responseBuilder.build());
            } else {
                return Optional.empty();
            }
        };
    }
}
