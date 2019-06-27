package io.payworks.labs.tcpmocker.support.definition;

import io.payworks.labs.tcpmocker.datahandler.DataHandler;

public interface DataHandlerProvider {
    DataHandler getDataHandler();
}
