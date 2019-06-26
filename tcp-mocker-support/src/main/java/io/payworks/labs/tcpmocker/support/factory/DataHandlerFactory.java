package io.payworks.labs.tcpmocker.support.factory;

import io.payworks.labs.tcpmocker.datahandler.DataHandler;

import java.io.InputStream;

public interface DataHandlerFactory {
    DataHandler create(InputStream src);
}
