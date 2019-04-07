package io.payworks.labs.tcpmocker.datahandler.supplier;

import io.payworks.labs.tcpmocker.datahandler.DataHandler;

import java.util.Collection;

@FunctionalInterface
public interface DataHandlersSupplier {

    Collection<? extends DataHandler> get();

}
