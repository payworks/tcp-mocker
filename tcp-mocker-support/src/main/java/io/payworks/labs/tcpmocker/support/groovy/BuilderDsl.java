package io.payworks.labs.tcpmocker.support.groovy;

import io.payworks.labs.tcpmocker.support.builder.BinaryBuilder;
import io.payworks.labs.tcpmocker.support.definition.DataHandlerBuilderProvider;

public final class BuilderDsl {
    private BuilderDsl() {
    }

    protected static BinaryBuilder binary() {
        return new BinaryBuilder();
    }

    public static DataHandlerBuilderProvider dataHandler() {
        return new DataHandlerBuilderProvider();
    }
}
