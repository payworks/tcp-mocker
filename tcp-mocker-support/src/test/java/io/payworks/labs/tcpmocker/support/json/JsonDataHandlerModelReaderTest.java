package io.payworks.labs.tcpmocker.support.json;

import io.payworks.labs.tcpmocker.support.definition.DataHandlerModel;
import io.payworks.labs.tcpmocker.support.resource.ResourceUtils;
import org.testng.annotations.Test;

import java.util.stream.Collectors;

import static io.payworks.labs.tcpmocker.test.TcpMappingsRegistry.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class JsonDataHandlerModelReaderTest {

    private final JsonDataHandlerModelReader modelReader = new JsonDataHandlerModelReader();

    @Test
    public void readTestDefaultJsonMapping() {
        final DataHandlerModel dataHandlerModel = modelReader.readDataHandlerModel(ResourceUtils.getInputStream(TEST_DEFAULT_JSON_MAPPING_1));
        assertThat(dataHandlerModel, notNullValue());

        assertThat(dataHandlerModel.getRequest().getMatches(), equalToIgnoringCase("0200\\d{10}4F2F0F90000030"));
        assertThat(dataHandlerModel.getResponse().getData(), equalToIgnoringCase("ad122e1b75356c6fdf3e9c3076a80da6"));
    }

    @Test
    public void readTestListJsonMapping() {
        final DataHandlerModel dataHandlerModel = modelReader.readDataHandlerModel(ResourceUtils.getInputStream(TEST_LIST_JSON_MAPPING_1));
        assertThat(dataHandlerModel, notNullValue());

        assertThat(dataHandlerModel.getRequest().getMatches(), equalToIgnoringCase("0200\\d{10}4F2F0F90000030"));
        assertThat(dataHandlerModel.getResponse().getData(), equalToIgnoringCase("ad122e1b75356c6fdf3e9c3076a80da6"));
    }

    @Test
    public void readTestMultiRequestJsonMapping() {
        final DataHandlerModel dataHandlerModel = modelReader.readDataHandlerModel(ResourceUtils.getInputStream(TEST_MULTIREQUEST_JSON_MAPPING_1));
        assertThat(dataHandlerModel, notNullValue());

        assertThat(dataHandlerModel.getRequestList().stream().map(DataHandlerModel.Request::getMatches).collect(Collectors.toList()), contains("0200\\d{10}4F2F0F90000030", "0201\\d{10}4F2F0F90000030"));
        assertThat(dataHandlerModel.getResponse().getData(), equalToIgnoringCase("ad122e1b75356c6fdf3e9c3076a80da611"));
    }
}