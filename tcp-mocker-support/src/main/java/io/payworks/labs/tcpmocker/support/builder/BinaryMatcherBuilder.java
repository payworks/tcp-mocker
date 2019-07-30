package io.payworks.labs.tcpmocker.support.builder;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class BinaryMatcherBuilder {

    private static final Charset EBCDIC_1047_CHARSET = Charset.forName("cp1047");

    private final List<byte[]> matchers = new ArrayList<>();

    public BinaryMatcherBuilder ascii(final String str) {
        matchers.add(str.getBytes(StandardCharsets.US_ASCII));
        return this;
    }

    public BinaryMatcherBuilder ebcdic(final String str) {
        matchers.add(str.getBytes(EBCDIC_1047_CHARSET));
        return this;
    }

    public BinaryMatcherBuilder stx() {
        matchers.add(new byte[]{0x02});
        return this;
    }

    public BinaryMatcherBuilder etx() {
        matchers.add(new byte[]{0x03});
        return this;
    }

    public byte[] build() {
        final int length = matchers.stream()
                .map(arr -> arr.length)
                .reduce(0, Integer::sum);

        final byte[] data = new byte[length];

        int destPos = 0;
        for (final byte[] matcher : matchers) {
            System.arraycopy(matcher, 0, data, destPos, matcher.length);
            destPos = destPos + matcher.length;
        }

        return data;
    }
}
