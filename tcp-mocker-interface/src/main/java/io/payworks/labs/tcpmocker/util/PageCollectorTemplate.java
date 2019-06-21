package io.payworks.labs.tcpmocker.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

// TODO: create -util module for this class
public final class PageCollectorTemplate {

    private PageCollectorTemplate() {
    }

    public static <ENTITY> List<ENTITY> collectAllPages(
            final BiFunction<Integer, Integer, List<ENTITY>> pageSupplier,
            final int pageSize) {
        final List<ENTITY> entities = new ArrayList<>();

        for (int pageNumber = 0; ; ++pageNumber) {
            final List<ENTITY> page = pageSupplier.apply(pageNumber, pageSize);
            if (page.isEmpty()) {
                break;
            }
            entities.addAll(page);
        }
        return entities;
    }

    public static <ENTITY, PAGE> List<ENTITY> collectAllPages(
            final Function<PAGE, List<ENTITY>> pageSupplier,
            final BiFunction<Integer, Integer, PAGE> pageableBuilder,
            final int pageSize) {
        final List<ENTITY> entities = new ArrayList<>();

        for (int pageNumber = 0; ; ++pageNumber) {
            final List<ENTITY> page = pageSupplier.apply(pageableBuilder.apply(pageNumber, pageSize));
            if (page.isEmpty()) {
                break;
            }
            entities.addAll(page);
        }
        return entities;
    }
}
