package io.payworks.labs.tcpmocker.support.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.payworks.labs.tcpmocker.support.definition.DataHandlerModel;

import java.io.IOException;
import java.io.InputStream;

public class JsonDataHandlerModelReader {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public DataHandlerModel readDataHandlerModel(InputStream src) {
        try {
            return objectMapper.readValue(src, DataHandlerModel.class);
        } catch (final IOException e) {
            throw new JsonDataHandlerModelReaderException(e);
        }
    }
}
