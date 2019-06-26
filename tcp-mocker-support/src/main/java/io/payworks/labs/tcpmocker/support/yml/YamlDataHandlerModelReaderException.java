package io.payworks.labs.tcpmocker.support.yml;

public class YamlDataHandlerModelReaderException extends RuntimeException {

    public YamlDataHandlerModelReaderException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public YamlDataHandlerModelReaderException(final Throwable cause) {
        super(cause);
    }
}
