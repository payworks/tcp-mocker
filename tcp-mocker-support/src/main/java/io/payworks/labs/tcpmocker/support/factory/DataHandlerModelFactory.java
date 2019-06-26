package io.payworks.labs.tcpmocker.support.factory;

import io.payworks.labs.tcpmocker.datahandler.DataHandler;
import io.payworks.labs.tcpmocker.support.mapping.DataHandlerModel;

public interface DataHandlerModelFactory {
    DataHandler createDataHandler(DataHandlerModel dataHandlerModel);
}
