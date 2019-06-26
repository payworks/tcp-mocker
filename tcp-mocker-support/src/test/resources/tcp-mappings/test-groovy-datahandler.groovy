import io.payworks.labs.tcpmocker.support.groovy.GroovyDataHandler

import static java.nio.charset.StandardCharsets.UTF_8

class TestGroovyDataHadler extends GroovyDataHandler {

    @Override
    protected boolean matches(final byte[] request) {
        return Arrays.equals("ping".getBytes(UTF_8), request)
    }

    @Override
    protected byte[] response() {
        return "pong".getBytes(UTF_8)
    }
}

