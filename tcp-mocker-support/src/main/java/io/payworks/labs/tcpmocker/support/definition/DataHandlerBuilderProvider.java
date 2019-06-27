package io.payworks.labs.tcpmocker.support.definition;

import io.payworks.labs.tcpmocker.datahandler.DataHandler;
import io.payworks.labs.tcpmocker.support.builder.DataHandlerBuilder;

public abstract class DataHandlerBuilderProvider implements DataHandlerProvider {

    protected abstract DataHandlerBuilder dataHandler();

    public DataHandler getDataHandler() {
        return dataHandler().build();
    }
}
