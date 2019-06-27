package io.payworks.labs.tcpmocker.support.yml;

import io.payworks.labs.tcpmocker.support.definition.DataHandlerModel;
import io.payworks.labs.tcpmocker.support.resource.ResourceUtils;
import org.testng.annotations.Test;

import java.util.stream.Collectors;

import static io.payworks.labs.tcpmocker.test.TcpMappingsRegistry.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class YamlDataHandlerModelReaderTest {
    private final YamlDataHandlerModelReader modelReader = new YamlDataHandlerModelReader();

    @Test
    public void testReadDefaultYamlMapping() {
        final DataHandlerModel dataHandlerModel = modelReader.readDataHandlerModel(ResourceUtils.getInputStream(TEST_DEFAULT_YAML_MAPPING_1));
        assertThat(dataHandlerModel, notNullValue());

        assertThat(dataHandlerModel.getRequest().getMatches(), equalToIgnoringCase("0200\\d{10}4F2F0F90000030"));
        assertThat(dataHandlerModel.getResponse().getData(), equalToIgnoringCase("ad122e1b75356c6fdf3e9c3076a80da6"));
    }

    @Test
    public void readTestListYamlMapping() {
        final DataHandlerModel dataHandlerModel = modelReader.readDataHandlerModel(ResourceUtils.getInputStream(TEST_LIST_YAML_MAPPING_1));
        assertThat(dataHandlerModel, notNullValue());

        assertThat(dataHandlerModel.getRequest().getMatches(), equalToIgnoringCase("0200\\d{10}4F2F0F90000030"));
        assertThat(dataHandlerModel.getResponse().getData(), equalToIgnoringCase("ad122e1b75356c6fdf3e9c3076a80da6"));
    }

    @Test
    public void readTestMultiRequestYamlMapping() {
        final DataHandlerModel dataHandlerModel = modelReader.readDataHandlerModel(ResourceUtils.getInputStream(TEST_MULTIREQUEST_YAML_MAPPING_1));
        assertThat(dataHandlerModel, notNullValue());

        assertThat(dataHandlerModel.getRequestList().stream().map(DataHandlerModel.Request::getMatches).collect(Collectors.toList()), contains("0200\\d{10}4F2F0F90000030", "0201\\d{10}4F2F0F90000030"));
        assertThat(dataHandlerModel.getResponse().getData(), equalToIgnoringCase("ad122e1b75356c6fdf3e9c3076a80da612"));
    }

}