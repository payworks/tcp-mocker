package io.payworks.labs.tcpmocker.recording;

import io.payworks.labs.tcpmocker.datahandler.DataHandler;

import java.util.Optional;

public class RecordingDataHandler implements DataHandler {

    private final RecordingsRepository recordingsRepository;

    private final DataHandler delegateDataHandler;

    public RecordingDataHandler(final RecordingsRepository recordingsRepository,
                                final DataHandler delegateDataHandler) {
        this.recordingsRepository = recordingsRepository;
        this.delegateDataHandler = delegateDataHandler;
    }

    @Override
    public Optional<byte[]> handle(final byte[] data) {
        return delegateDataHandler.handle(data)
                .map(response -> RecordingEntity.builder()
                        .withRequest(data)
                        .withReply(response)
                        .build())
                .map(recordingsRepository::save)
                .map(RecordingEntity::getReply);
    }
}
