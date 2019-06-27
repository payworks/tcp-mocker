package io.payworks.labs.tcpmocker.support;

import com.google.common.collect.ImmutableMap;
import io.payworks.labs.tcpmocker.datahandler.DataHandler;
import io.payworks.labs.tcpmocker.support.factory.DataHandlerFactory;
import io.payworks.labs.tcpmocker.support.factory.DefaultDataHandlerModelFactory;
import io.payworks.labs.tcpmocker.support.groovy.GroovyDataHandlerFactory;
import io.payworks.labs.tcpmocker.support.json.JsonDataHandlerFactory;
import io.payworks.labs.tcpmocker.support.resource.DefaultResourceLoader;
import io.payworks.labs.tcpmocker.support.resource.ResourceLoader;
import io.payworks.labs.tcpmocker.support.yml.YamlDataHandlerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class DefaultDataHandlersLoader implements DataHandlersLoader {

    public static final ImmutableMap<Pattern, DataHandlerFactory> DATA_HANDLER_FACTORIES = ImmutableMap.of(
            Pattern.compile(".+\\.json"), new JsonDataHandlerFactory(new DefaultDataHandlerModelFactory()),
            Pattern.compile(".+\\.ya?ml"), new YamlDataHandlerFactory(new DefaultDataHandlerModelFactory()),
            Pattern.compile(".+\\.grdh"), new GroovyDataHandlerFactory()
    );
    public static final String DEFAULT_MAPPINGS_PATH = "classpath:/tcp-mappings/";

    private final ResourceLoader resourceLoader;

    private String mappingsPath = DEFAULT_MAPPINGS_PATH;
    private Map<Pattern, DataHandlerFactory> dataHandlerFactories = DATA_HANDLER_FACTORIES;

    public DefaultDataHandlersLoader(final ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public DefaultDataHandlersLoader() {
        this(new DefaultResourceLoader());
    }

    public void setMappingsPath(final String mappingsPath) {
        this.mappingsPath = mappingsPath;
    }

    public void setDataHandlerFactories(final Map<Pattern, DataHandlerFactory> dataHandlerFactories) {
        this.dataHandlerFactories = ImmutableMap.copyOf(dataHandlerFactories);
    }

    @Override
    public Map<String, DataHandler> dataHandlers(final Set<String> filter) {
        final List<String> dirList = resourceLoader.list(mappingsPath);

        final Map<String, DataHandler> dataHandlers = new LinkedHashMap<>();

        dirList.forEach(filePath -> {
            dataHandlers.putAll(filePathDataHandlers(filter, filePath));
        });

        return Collections.unmodifiableMap(dataHandlers);
    }

    private Map<String, DataHandler> filePathDataHandlers(final Set<String> filter, final String filePath) {
        final List<Pattern> patterns = dataHandlerFactories.keySet().stream()
                .filter(patternPredicate -> !filter.contains(filePath) && patternPredicate.asPredicate().test(filePath))
                .collect(Collectors.toList());

        final ImmutableMap.Builder<String, DataHandler> dataHandlers = ImmutableMap.builder();
        for (final Pattern pattern : patterns) {
            try (final InputStream inputStream = resourceLoader.getInputStream(filePath)) {

                final DataHandler dataHandler = dataHandlerFactories.get(pattern).create(inputStream);

                dataHandlers.put(filePath, dataHandler);
            } catch (final IOException e) {
                throw new DataHandlersLoaderException(e);
            }
        }
        return dataHandlers.build();
    }
}
