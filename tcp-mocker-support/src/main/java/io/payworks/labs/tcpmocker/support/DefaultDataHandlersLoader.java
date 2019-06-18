package io.payworks.labs.tcpmocker.support;

import com.google.common.collect.ImmutableMap;
import io.payworks.labs.tcpmocker.datahandler.DataHandler;
import io.payworks.labs.tcpmocker.support.datahandlermodel.DataHandlerModel;
import io.payworks.labs.tcpmocker.support.datahandlermodel.DataHandlerModelReader;
import io.payworks.labs.tcpmocker.support.json.JsonMappingReader;
import io.payworks.labs.tcpmocker.support.resource.DefaultResourceLoader;
import io.payworks.labs.tcpmocker.support.resource.ResourceLoader;
import io.payworks.labs.tcpmocker.support.yml.YamlMappingReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class DefaultDataHandlersLoader implements DataHandlersLoader {

    public static final ImmutableMap<Pattern, DataHandlerModelReader> DEFAULT_READERS_MAP = ImmutableMap.of(
            Pattern.compile(".+\\.json"), new JsonMappingReader(),
            Pattern.compile(".+\\.ya?ml"), new YamlMappingReader()
    );
    public static final String DEFAULT_MAPPINGS_PATH = "classpath:/tcp-mappings/";

    private final ResourceLoader resourceLoader;
    private final DataHandlerFactory dataHandlerFactory = new DataHandlerFactory();

    private String mappingsPath = DEFAULT_MAPPINGS_PATH;
    private Map<Pattern, DataHandlerModelReader> readersMap = DEFAULT_READERS_MAP;

    public DefaultDataHandlersLoader(final ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public DefaultDataHandlersLoader() {
        this(new DefaultResourceLoader());
    }

    public void setMappingsPath(final String mappingsPath) {
        this.mappingsPath = mappingsPath;
    }

    public void setReadersMap(final Map<Pattern, DataHandlerModelReader> readersMap) {
        this.readersMap = ImmutableMap.copyOf(readersMap);
    }

    @Override
    public Map<String, DataHandler> dataHandlers(final Set<String> filter) {
        final List<String> dirList = resourceLoader.list(mappingsPath);

        final Map<String, DataHandler> dataHandlers = new LinkedHashMap<>();

        readersMap.forEach((pattern, reader) -> {
            final Predicate<String> patternPredicate = pattern.asPredicate();
            final List<String> filePaths = dirList.stream()
                    .filter(filePath -> !filter.contains(filePath) && patternPredicate.test(filePath))
                    .collect(Collectors.toList());

            for (final String filePath : filePaths) {
                try (final InputStream inputStream = resourceLoader.getInputStream(filePath)) {
                    final DataHandlerModel dataHandlerModel = reader.read(inputStream);

                    dataHandlers.put(filePath, dataHandlerFactory.createDataHandler(dataHandlerModel));
                } catch (final IOException e) {
                    throw new DataHandlersLoaderException(e);
                }
            }
        });

        return Collections.unmodifiableMap(dataHandlers);
    }
}
