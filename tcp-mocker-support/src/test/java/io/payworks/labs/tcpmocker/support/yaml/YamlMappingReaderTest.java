package io.payworks.labs.tcpmocker.support.yaml;

import io.payworks.labs.tcpmocker.support.datahandlermodel.DataHandlerModel;
import io.payworks.labs.tcpmocker.support.yml.YamlMappingReader;
import org.testng.annotations.Test;

import java.util.stream.Collectors;

import static io.payworks.labs.tcpmocker.test.TcpMappingsRegistry.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class YamlMappingReaderTest {

    private final YamlMappingReader mappingReader = new YamlMappingReader();

    @Test
    public void testReadDefaultYamlMapping() {
        final DataHandlerModel dataHandlerModel = mappingReader.urlRead(TEST_DEFAULT_YAML_MAPPING_1);
        assertThat(dataHandlerModel, notNullValue());

        assertThat(dataHandlerModel.getRequest().getMatches(), equalToIgnoringCase("0200\\d{10}4F2F0F90000030"));
        assertThat(dataHandlerModel.getResponse().getData(), equalToIgnoringCase("ad122e1b75356c6fdf3e9c3076a80da6"));
    }

    @Test
    public void readTestListYamlMapping() {
        final DataHandlerModel dataHandlerModel = mappingReader.urlRead(TEST_LIST_YAML_MAPPING_1);
        assertThat(dataHandlerModel, notNullValue());

        assertThat(dataHandlerModel.getRequest().getMatches(), equalToIgnoringCase("0200\\d{10}4F2F0F90000030"));
        assertThat(dataHandlerModel.getResponse().getData(), equalToIgnoringCase("ad122e1b75356c6fdf3e9c3076a80da6"));
    }

    @Test
    public void readTestMultiRequestYamlMapping() {
        final DataHandlerModel dataHandlerModel = mappingReader.urlRead(TEST_MULTIREQUEST_YAML_MAPPING_1);
        assertThat(dataHandlerModel, notNullValue());

        assertThat(dataHandlerModel.getRequestList().stream().map(DataHandlerModel.Request::getMatches).collect(Collectors.toList()), contains("0200\\d{10}4F2F0F90000030", "0201\\d{10}4F2F0F90000030"));
        assertThat(dataHandlerModel.getResponse().getData(), equalToIgnoringCase("ad122e1b75356c6fdf3e9c3076a80da612"));
    }

}