package io.payworks.labs.tcpmocker.support.factory;

import io.payworks.labs.tcpmocker.datahandler.*;
import io.payworks.labs.tcpmocker.order.Ordered;
import io.payworks.labs.tcpmocker.support.definition.DataHandlerModel;

import java.util.List;
import java.util.stream.Collectors;

public final class DefaultDataHandlerModelFactory implements DataHandlerModelFactory {

    @Override
    public DataHandler createDataHandler(final DataHandlerModel dataHandlerModel) {
        final DataHandlerType dataHandlerType = getDataHandlerType(dataHandlerModel);
        final String responseData = dataHandlerModel.getResponse().getData();
        final List<DataHandlerModel.Request> requestsToMatch = dataHandlerModel.getRequestList();
        final int order = getOrder(dataHandlerModel);

        final List<DataHandler> dataHandlers = requestsToMatch.stream().map(DataHandlerModel.Request::getMatches)
                .map(hexRegex -> createHexRegexDataHandler(dataHandlerType, hexRegex, responseData))
                .collect(Collectors.toList());

        return new OrderedDataHandler(order, new CompositeDataHandler(dataHandlers));
    }

    private static HexRegexDataHandler createHexRegexDataHandler(final DataHandlerType dataHandlerType,
                                                                 final String hexRegex,
                                                                 final String responseData) {
        switch (dataHandlerType) {
            case DYNAMIC_HEX_REGEX:
                return new DynamicHexRegexDataHandler(hexRegex, responseData);

            case STATIC_HEX_REGEX:
                return new StaticHexRegexDataHandler(hexRegex, responseData);

            default:
                throw new UnsupportedOperationException(String.format("Unknown DataHandler Type: %s", dataHandlerType));
        }
    }

    private static int getOrder(final DataHandlerModel dataHandlerModel) {
        final Integer order = dataHandlerModel.getOrder();
        return order == null ? Ordered.DEFAULT_ORDER : order;
    }

    private static DataHandlerType getDataHandlerType(final DataHandlerModel dataHandlerModel) {
        final DataHandlerType handlerType = dataHandlerModel.getHandlerType();
        return handlerType == null ? DataHandlerType.STATIC_HEX_REGEX : handlerType;
    }
}
