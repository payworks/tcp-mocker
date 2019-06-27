package io.payworks.labs.tcpmocker.support;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class DataBuilder {

    private List<byte[]> parts = new ArrayList<>();

    public DataBuilder ascii(final String str) {
        parts.add(str.getBytes(StandardCharsets.US_ASCII));
        return this;
    }

    public DataBuilder stx() {
        parts.add(new byte[]{0x02});
        return this;
    }

    public DataBuilder etx() {
        parts.add(new byte[]{0x03});
        return this;
    }

    public byte[] build() {
        final int length = parts.stream().map(arr -> arr.length).reduce(0, Integer::sum);
        final byte[] data = new byte[length];

        int pos = 0;
        for (final byte[] part : parts) {
            System.arraycopy(part, 0, data, pos, part.length);
            pos = pos + part.length;
        }

        return data;
    }
}
