package io.payworks.labs.tcpmocker.support;

import io.payworks.labs.tcpmocker.TcpServer;
import io.payworks.labs.tcpmocker.TcpServerBuilder;
import io.payworks.labs.tcpmocker.datahandler.DataHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static java.util.stream.Collectors.joining;

public final class TcpServerFactory {

    private static final Logger logger = LoggerFactory.getLogger(TcpServerFactory.class);

    private final TcpServerBuilder<? extends TcpServer> serverBuilder;
    private final DataHandlersLoader dataHandlersLoader;

    public TcpServerFactory(final TcpServerBuilder<? extends TcpServer> serverBuilder,
                            final DataHandlersLoader dataHandlersLoader) {
        this.serverBuilder = serverBuilder;
        this.dataHandlersLoader = dataHandlersLoader;
    }

    public TcpServer createTcpServer(final int port) {
        final Map<String, ? extends DataHandler> dataHandlersMap = dataHandlersLoader.dataHandlers();

        final String dataHandlersList = dataHandlersMap.keySet()
                .stream()
                .map(key -> String.format("\t- %s", key))
                .collect(joining("\n"));

        if (dataHandlersMap.isEmpty()) {
            logger.warn("No Data Handlers!");
        } else {
            logger.info("Data Handlers ({}):\n{}", dataHandlersMap.size(), dataHandlersList);
        }

        serverBuilder.withDataHandlersSupplier(
                () -> dataHandlersLoader.dataHandlers().values()
        );

        serverBuilder.withPort(port);

        final TcpServer server = serverBuilder.build();

        logger.info("Server Started (:{})", server.getPort());

        return server;
    }
}
