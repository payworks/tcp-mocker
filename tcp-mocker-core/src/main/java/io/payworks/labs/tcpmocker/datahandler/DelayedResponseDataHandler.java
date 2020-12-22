package io.payworks.labs.tcpmocker.datahandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DelayedResponseDataHandler implements DataHandler {

    private static final Logger logger = LoggerFactory.getLogger(DelayedResponseDataHandler.class);

    private final Supplier<Duration> responseDelaySupplier;
    private final DataHandler delegate;
    private final Consumer<Duration> delayer;

    public DelayedResponseDataHandler(final Supplier<Duration> responseDelaySupplier,
                               final DataHandler delegate) {
        this(responseDelaySupplier, delegate, DelayedResponseDataHandler::delayFor);
    }

    DelayedResponseDataHandler(final Supplier<Duration> responseDelaySupplier,
                               final DataHandler delegate,
                               final Consumer<Duration> delayer) {
        this.responseDelaySupplier = responseDelaySupplier;
        this.delegate = delegate;
        this.delayer = delayer;
    }

    @Override
    public Optional<byte[]> handle(final byte[] data) {
        final var response = delegate.handle(data);
        if (response.isPresent()) {
            applyDelay();
        }
        return response;
    }

    private void applyDelay() {
        final var delayDuration = responseDelaySupplier.get();

        if (!delayDuration.isZero()) {
            logger.info("Delaying response for {}", delayDuration);
            delayer.accept(delayDuration);
        }
    }

    private static void delayFor(final Duration duration) {
        try {
            TimeUnit.MILLISECONDS.sleep(duration.toMillis());
        } catch (final InterruptedException ex) {
            logger.error("Delay on the response interrupted because of {}", ex.getMessage(), ex);
        }
    }
}
