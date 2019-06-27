package io.payworks.labs.tcpmocker.support.groovy;

import io.payworks.labs.tcpmocker.datahandler.DataHandler;
import io.payworks.labs.tcpmocker.support.definition.DataHandlerProvider;
import io.payworks.labs.tcpmocker.support.factory.DataHandlerFactory;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class GroovyDataHandlerFactory implements DataHandlerFactory {

    private final GroovyDataHandlerCompiler groovyDataHandlerCompiler;

    public GroovyDataHandlerFactory(final GroovyDataHandlerCompiler groovyDataHandlerCompiler) {
        this.groovyDataHandlerCompiler = groovyDataHandlerCompiler;
    }

    public GroovyDataHandlerFactory() {
        this(new GroovyDataHandlerCompiler());
    }

    @Override
    public DataHandler create(final InputStream src) {
        try {
            final String groovyScript = IOUtils.toString(src, StandardCharsets.UTF_8);
            final DataHandlerProvider dataHandlerProvider = groovyDataHandlerCompiler.compile(groovyScript);
            return dataHandlerProvider.getDataHandler();
        } catch (final IOException e) {
            throw new GroovyDataHandlerFactoryException(e);
        }
    }
}
