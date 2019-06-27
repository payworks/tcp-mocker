package io.payworks.labs.tcpmocker.support.yml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.payworks.labs.tcpmocker.support.definition.DataHandlerModel;

import java.io.IOException;
import java.io.InputStream;

public class YamlDataHandlerModelReader {

    private final ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

    public DataHandlerModel readDataHandlerModel(InputStream src) {
        try {
            return objectMapper.readValue(src, DataHandlerModel.class);
        } catch (final IOException e) {
            throw new YamlDataHandlerModelReaderException(e);
        }
    }
}
