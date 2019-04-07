package io.payworks.labs.tcpmocker;

import io.payworks.labs.tcpmocker.datahandler.DataHandler;
import io.payworks.labs.tcpmocker.datahandler.supplier.DataHandlersSupplier;

import java.util.Collection;

public abstract class AbstractTcpServerBuilder<SERVER extends TcpServer, BUILDER extends AbstractTcpServerBuilder<SERVER, BUILDER>> implements TcpServerBuilder<SERVER> {

    private DataHandlersSupplier dataHandlersSupplier;
    private int port;

    protected abstract BUILDER self();

    @Override
    public BUILDER withDataHandlersSupplier(final DataHandlersSupplier dataHandlersSupplier) {
        this.dataHandlersSupplier = dataHandlersSupplier;
        return self();
    }

    @Override
    public BUILDER withPort(final int port) {
        this.port = port;
        return self();
    }

    protected Collection<? extends DataHandler> getDataHandlers() {
        return dataHandlersSupplier.get();
    }

    int getPort() {
        return port;
    }
}
