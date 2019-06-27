package io.payworks.labs.tcpmocker.support.groovy;

import groovy.lang.GroovyShell;
import io.payworks.labs.tcpmocker.support.definition.DataHandlerProvider;

public class GroovyDataHandlerCompiler {

    private final GroovyShell groovyShell;

    public GroovyDataHandlerCompiler(final GroovyShell groovyShell) {
        this.groovyShell = groovyShell;
    }

    public GroovyDataHandlerCompiler() {
        this(new GroovyShell());
    }

    @SuppressWarnings("unchecked")
    public DataHandlerProvider compile(final String groovyDataHandlerContent) {
        if (groovyDataHandlerContent == null || groovyDataHandlerContent.trim().isEmpty()) {
            throw new IllegalArgumentException("Unable to parse Groovy Data Handler file because is either null or empty!");
        }

        return (DataHandlerProvider) groovyShell.evaluate(groovyDataHandlerContent);
    }
}
