package io.payworks.labs.tcpmocker.support.yml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.payworks.labs.tcpmocker.support.datahandlermodel.DataHandlerModel;
import io.payworks.labs.tcpmocker.support.datahandlermodel.DataHandlerModelReader;
import io.payworks.labs.tcpmocker.support.json.JsonMappingReaderException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class YamlMappingReader implements DataHandlerModelReader {

    private final ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

    public DataHandlerModel read(final InputStream src) {
        try {
            return objectMapper.readValue(src, DataHandlerModel.class);
        } catch (final IOException e) {
            throw new JsonMappingReaderException(e);
        }
    }

    public DataHandlerModel read(final URL src) {
        try {
            return objectMapper.readValue(src, DataHandlerModel.class);
        } catch (final IOException e) {
            throw new JsonMappingReaderException(e);
        }
    }
}
