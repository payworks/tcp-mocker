package io.payworks.labs.tcpmocker.support.yml;

import io.payworks.labs.tcpmocker.datahandler.DataHandler;
import io.payworks.labs.tcpmocker.support.factory.DataHandlerFactory;
import io.payworks.labs.tcpmocker.support.factory.DataHandlerModelFactory;
import io.payworks.labs.tcpmocker.support.definition.DataHandlerModel;

import java.io.InputStream;

public class YamlDataHandlerFactory implements DataHandlerFactory {

    private final YamlDataHandlerModelReader reader = new YamlDataHandlerModelReader();
    private final DataHandlerModelFactory dataHandlerModelFactory;

    public YamlDataHandlerFactory(final DataHandlerModelFactory dataHandlerModelFactory) {
        this.dataHandlerModelFactory = dataHandlerModelFactory;
    }

    public DataHandler create(final InputStream src) {
        final DataHandlerModel dataHandlerModel = reader.readDataHandlerModel(src);
        return dataHandlerModelFactory.createDataHandler(dataHandlerModel);
    }
}