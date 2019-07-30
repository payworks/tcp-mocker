package io.payworks.labs.tcpmocker.support.builder;

import io.payworks.labs.tcpmocker.datahandler.DataHandler;

import java.util.Arrays;
import java.util.Optional;

public class DataHandlerBuilder {

    private BinaryMatcherBuilder requestBuilder;
    private BinaryMatcherBuilder responseBuilder;

    public DataHandlerBuilder request(final BinaryMatcherBuilder requestBuilder) {
        this.requestBuilder = requestBuilder;
        return this;
    }

    public DataHandlerBuilder response(final BinaryMatcherBuilder responseBuilder) {
        this.responseBuilder = responseBuilder;
        return this;
    }

    public DataHandler build() {
        if (requestBuilder == null) requestBuilder = new BinaryMatcherBuilder();
        if (responseBuilder == null) responseBuilder = new BinaryMatcherBuilder();

        return data -> {
            if (Arrays.equals(requestBuilder.build(), data)) {
                return Optional.of(responseBuilder.build());
            } else {
                return Optional.empty();
            }
        };
    }
}
