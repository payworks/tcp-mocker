package io.payworks.labs.tcpmocker.support.groovy;

import io.payworks.labs.tcpmocker.support.builder.BinaryMatcherBuilder;
import io.payworks.labs.tcpmocker.support.definition.DataHandlerBuilderProvider;

public final class BuilderDsl {
    private BuilderDsl() {
    }

    public static BinaryMatcherBuilder binary() {
        return new BinaryMatcherBuilder();
    }

    public static DataHandlerBuilderProvider dataHandler() {
        return new DataHandlerBuilderProvider();
    }
}
