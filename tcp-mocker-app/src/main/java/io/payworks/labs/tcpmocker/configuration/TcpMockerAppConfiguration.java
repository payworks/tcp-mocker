package io.payworks.labs.tcpmocker.configuration;

import io.payworks.labs.tcpmocker.NettyTcpServerBuilder;
import io.payworks.labs.tcpmocker.TcpServer;
import io.payworks.labs.tcpmocker.datahandler.CompositeDataHandler;
import io.payworks.labs.tcpmocker.datahandler.DataHandlerDispatcherFactory;
import io.payworks.labs.tcpmocker.datahandler.LoggingDataHandler;
import io.payworks.labs.tcpmocker.properties.TcpMockerProperties;
import io.payworks.labs.tcpmocker.recording.RecordingDataHandler;
import io.payworks.labs.tcpmocker.recording.RecordingsRepository;
import io.payworks.labs.tcpmocker.support.DataHandlersLoader;
import io.payworks.labs.tcpmocker.support.DefaultDataHandlersLoader;
import io.payworks.labs.tcpmocker.support.TcpServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TcpMockerAppConfiguration {

    private final TcpMockerProperties properties;

    public TcpMockerAppConfiguration(final TcpMockerProperties properties) {
        this.properties = properties;
    }

    @Bean
    public DataHandlersLoader dataHandlersLoader() {
        final DefaultDataHandlersLoader dataHandlersLoader = new DefaultDataHandlersLoader();

        dataHandlersLoader.setMappingsPath(properties.getMappingsPath());

        return dataHandlersLoader;
    }

    @Bean
    public TcpServer tcpServer(final DataHandlerDispatcherFactory dataHandlerDispatcherFactory) {
        final TcpServerFactory serverFactory = new TcpServerFactory(
                new NettyTcpServerBuilder()
                        .withDataHandlerDispatcherFactory(dataHandlerDispatcherFactory),
                dataHandlersLoader());

        return serverFactory.createTcpServer(properties.getListenPort());
    }

    @Bean
    public DataHandlerDispatcherFactory dataHandlerDispatcherFactory(final RecordingsRepository recordingsRepository) {
        return dataHandlers ->
                new LoggingDataHandler(
                        new RecordingDataHandler(recordingsRepository, new CompositeDataHandler(dataHandlers)));
    }
}
