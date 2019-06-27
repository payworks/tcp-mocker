package io.payworks.labs.tcpmocker.support.definition;

import io.payworks.labs.tcpmocker.datahandler.DataHandler;
import io.payworks.labs.tcpmocker.support.builder.DataHandlerBuilder;

public class DataHandlerBuilderProvider extends DataHandlerBuilder implements DataHandlerProvider {
    public DataHandler getDataHandler() {
        return build();
    }
}
