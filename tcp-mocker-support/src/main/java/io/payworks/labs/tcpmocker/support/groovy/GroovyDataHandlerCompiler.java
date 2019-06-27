package io.payworks.labs.tcpmocker.support.groovy;

import groovy.lang.GroovyClassLoader;
import io.payworks.labs.tcpmocker.support.definition.DataHandlerProvider;

import java.lang.reflect.InvocationTargetException;

public class GroovyDataHandlerCompiler {

    private final GroovyClassLoader groovyClassLoader;

    public GroovyDataHandlerCompiler(final GroovyClassLoader groovyClassLoader) {
        this.groovyClassLoader = groovyClassLoader;
    }

    public GroovyDataHandlerCompiler() {
        this(GroovyClassLoaderFactory.newGroovyClassLoaderViaPriviligedAction());
    }

    @SuppressWarnings("unchecked")
    public DataHandlerProvider compile(final String groovyDataHandlerProvider) {
        if (groovyDataHandlerProvider == null || groovyDataHandlerProvider.trim().isEmpty()) {
            throw new IllegalArgumentException("Unable to parse Groovy Data Handler definition file because is either null or empty!");
        }

        return createDataHandlerProvider(groovyDataHandlerProvider);
    }

    private DataHandlerProvider createDataHandlerProvider(final String groovyDataHandlerProvider) {
        try {
            return ((Class<DataHandlerProvider>) groovyClassLoader.parseClass(groovyDataHandlerProvider))
                    .getDeclaredConstructor()
                    .newInstance();
        } catch (final NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Unable to create DataHandlerProvider instance!", e);
        }
    }
}
