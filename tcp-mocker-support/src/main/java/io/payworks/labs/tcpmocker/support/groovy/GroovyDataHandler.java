package io.payworks.labs.tcpmocker.support.groovy;

import io.payworks.labs.tcpmocker.datahandler.DataHandler;
import io.payworks.labs.tcpmocker.support.DataBuilder;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public abstract class GroovyDataHandler implements DataHandler {

    protected abstract DataBuilder request();

    protected DataBuilder response() {
        return binary();
    }

    @Override
    public Optional<byte[]> handle(final byte[] data) {
        if (Arrays.equals(request().build(), data)) {
            return Optional.of(response().build());
        } else {
            return Optional.empty();
        }
    }

    protected static DataBuilder binary() {
        return new DataBuilder();
    }

    public static Builder dataHandler() {
        return new Builder();
    }

    public static class Builder {
        private DataBuilder requestDataBuilder;
        private DataBuilder responseDataBuilder;

        public Builder request(final DataBuilder requestDataBuilder) {
            this.requestDataBuilder = requestDataBuilder;
            return this;
        }

        public Builder response(final DataBuilder responseDataBuilder) {
            this.responseDataBuilder = responseDataBuilder;
            return this;
        }

        public DataHandler build() {
            Objects.requireNonNull(requestDataBuilder, "Request is expected to be defined");

            return new GroovyDataHandler() {
                @Override
                protected DataBuilder request() {
                    return requestDataBuilder;
                }

                @Override
                protected DataBuilder response() {
                    return Optional.ofNullable(responseDataBuilder)
                            .orElseGet(GroovyDataHandler::binary);
                }
            };
        }
    }
}
