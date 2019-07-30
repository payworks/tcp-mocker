package io.payworks.labs.tcpmocker.test.pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class PageableCollector {

    private PageableCollector() {
    }

    public static <ENTITY> List<ENTITY> collectToList(
            final BiFunction<Integer, Integer, List<ENTITY>> pageSupplier,
            final int pageSize) {
        final List<ENTITY> list = new ArrayList<>();

        for (int pageNumber = 0; ; ++pageNumber) {
            final List<ENTITY> page = pageSupplier.apply(pageNumber, pageSize);
            if (page.isEmpty()) {
                break;
            }
            list.addAll(page);
        }
        return list;
    }

    public static <ENTITY, PAGEABLE> List<ENTITY> collectToList(
            final Function<PAGEABLE, List<ENTITY>> pageSupplier,
            final BiFunction<Integer, Integer, PAGEABLE> pageableBuilder,
            final int pageSize) {
        final List<ENTITY> list = new ArrayList<>();

        for (int pageNumber = 0; ; ++pageNumber) {
            final List<ENTITY> page = pageSupplier.apply(pageableBuilder.apply(pageNumber, pageSize));
            if (page.isEmpty()) {
                break;
            }
            list.addAll(page);
        }

        return list;
    }
}
