package io.payworks.labs.tcpmocker.support.groovy;

import groovy.lang.GroovyClassLoader;
import io.payworks.labs.tcpmocker.datahandler.DataHandler;

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
    public DataHandler compile(final String groovyDataHandlerContent) {
        if (groovyDataHandlerContent == null || groovyDataHandlerContent.trim().isEmpty()) {
            throw new IllegalArgumentException("Unable to parse Groovy Data Handler file because is either null or empty!");
        }

        try {
            return ((Class<DataHandler>) groovyClassLoader.parseClass(groovyDataHandlerContent))
                    .getDeclaredConstructor()
                    .newInstance();
        } catch (final NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Unable to create GroovyDataHandler instance!", e);
        }
    }
}
