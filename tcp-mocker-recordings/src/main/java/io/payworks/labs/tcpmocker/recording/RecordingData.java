package io.payworks.labs.tcpmocker.recording;

import com.google.common.io.BaseEncoding;

import java.time.Instant;

public class RecordingData {

    private final Instant timestamp;
    private final String request;
    private final String reply;

    public RecordingData() {
        this.timestamp = null;
        this.request = null;
        this.reply = null;
    }

    private RecordingData(final Instant timestamp,
                          final String request,
                          final String reply) {
        this.timestamp = timestamp;
        this.request = request;
        this.reply = reply;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getRequest() {
        return request;
    }

    public String getReply() {
        return reply;
    }

    public static Builder create() {
        return new Builder();
    }

    public static RecordingData fromEntity(final RecordingEntity recordingEntity) {
        return RecordingData.create()
                .withTimestamp(recordingEntity.getTimestamp())
                .withRequest(recordingEntity.getRequest())
                .withReply(recordingEntity.getReply())
                .build();
    }

    public static final class Builder {
        private Instant timestamp;
        private String request;
        private String reply;

        public Builder withTimestamp(final Instant timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder withRequest(final byte[] request) {
            this.request = BaseEncoding.base16().encode(request);
            return this;
        }

        public Builder withRequest(final String request) {
            this.request = request;
            return this;
        }

        public Builder withReply(final byte[] reply) {
            this.reply = BaseEncoding.base16().encode(reply);
            return this;
        }

        public Builder withReply(final String reply) {
            this.reply = reply;
            return this;
        }

        public final RecordingData build() {
            return new RecordingData(timestamp, request, reply);
        }
    }
}
