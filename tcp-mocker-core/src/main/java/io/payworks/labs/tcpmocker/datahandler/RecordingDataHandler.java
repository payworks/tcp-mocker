package io.payworks.labs.tcpmocker.datahandler;

import io.payworks.labs.tcpmocker.model.Recording;
import io.payworks.labs.tcpmocker.model.RecordingsRepository;

import java.util.Optional;

// TODO: extract recordings from core/model to separate module
public class RecordingDataHandler implements DataHandler {

    // TODO: create interface to allow various implementations of storage
    private final RecordingsRepository repository;

    private final DataHandler delegate;

    public RecordingDataHandler(final RecordingsRepository repository,
                                final DataHandler delegate) {
        this.repository = repository;
        this.delegate = delegate;
    }

    @Override
    public Optional<byte[]> handle(final byte[] data) {
        return delegate.handle(data)
                .map(response -> Recording.builder()
                        .withRequest(data)
                        .withReply(response)
                        .build())
                .map(repository::save)
                .map(Recording::getReply);
    }
}
