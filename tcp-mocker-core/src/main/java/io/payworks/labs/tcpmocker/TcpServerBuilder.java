package io.payworks.labs.tcpmocker;

import io.payworks.labs.tcpmocker.datahandler.supplier.DataHandlersSupplier;

public interface TcpServerBuilder<T extends TcpServer> {

    TcpServerBuilder<T> withDataHandlersSupplier(DataHandlersSupplier dataHandlersSupplier);

    TcpServerBuilder<T> withPort(int port);

    T build();
}
