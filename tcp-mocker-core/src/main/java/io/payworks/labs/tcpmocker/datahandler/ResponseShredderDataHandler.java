package io.payworks.labs.tcpmocker.datahandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.function.Supplier;

public class ResponseShredderDataHandler implements DataHandler  {

    private static final Logger logger = LoggerFactory.getLogger(ResponseShredderDataHandler.class);

    private final Supplier<Boolean> shredResponseSupplier;
    private final DataHandler delegate;

    public ResponseShredderDataHandler(final Supplier<Boolean> shredResponseSupplier, final DataHandler delegate) {
        this.shredResponseSupplier = shredResponseSupplier;
        this.delegate = delegate;
    }

    @Override
    public Optional<byte[]> handle(final byte[] data) {
        final var response = delegate.handle(data);

        if (shredResponseSupplier.get()) {
            logger.info("Dropping response...");
            return Optional.empty();
        } else {
            return response;
        }
    }
}
