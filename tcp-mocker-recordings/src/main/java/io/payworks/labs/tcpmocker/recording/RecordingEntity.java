package io.payworks.labs.tcpmocker.recording;

import javax.persistence.*;
import java.time.Instant;
import java.util.Arrays;

@Entity
public class RecordingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Instant timestamp;

    @Column(nullable = false, length = 8192)
    private byte[] request;

    @Column(nullable = false, length = 8192)
    private byte[] reply;

    protected RecordingEntity() {
        // JPA
    }

    private RecordingEntity(final byte[] request,
                            final byte[] reply) {
        this.timestamp = Instant.now();
        this.request = request;
        this.reply = reply;
    }

    public Long getId() {
        return id;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public byte[] getRequest() {
        return request;
    }

    public byte[] getReply() {
        return reply;
    }

    @Override
    public String toString() {
        return "RecordingEntity{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", request=" + Arrays.toString(request) +
                ", reply=" + Arrays.toString(reply) +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private byte[] request;
        private byte[] reply;

        private Builder() {
        }

        public Builder withRequest(final byte[] request) {
            this.request = request;
            return this;
        }

        public Builder withReply(final byte[] reply) {
            this.reply = reply;
            return this;
        }

        public RecordingEntity build() {
            final RecordingEntity recordingEntity = new RecordingEntity(request, reply);

            this.request = null;
            this.reply = null;

            return recordingEntity;
        }
    }
}
