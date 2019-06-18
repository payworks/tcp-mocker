package io.payworks.labs.tcpmocker.support.datahandlermodel;

import io.payworks.labs.tcpmocker.support.resource.ResourceUtils;

import java.io.InputStream;
import java.net.URL;

public interface DataHandlerModelReader {

    DataHandlerModel read(InputStream src);

    DataHandlerModel read(URL src);

    default DataHandlerModel urlRead(final String src) {
        return read(ResourceUtils.toResourceUrl(src));
    }
}
