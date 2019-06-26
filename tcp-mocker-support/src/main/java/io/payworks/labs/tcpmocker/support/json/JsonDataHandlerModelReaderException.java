package io.payworks.labs.tcpmocker.support.json;

public class JsonDataHandlerModelReaderException extends RuntimeException {

    public JsonDataHandlerModelReaderException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public JsonDataHandlerModelReaderException(final Throwable cause) {
        super(cause);
    }
}
