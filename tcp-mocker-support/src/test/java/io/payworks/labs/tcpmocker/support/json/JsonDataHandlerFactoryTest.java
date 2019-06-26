package io.payworks.labs.tcpmocker.support.json;

import io.payworks.labs.tcpmocker.datahandler.DataHandler;
import io.payworks.labs.tcpmocker.support.factory.DataHandlerModelFactory;
import io.payworks.labs.tcpmocker.support.resource.ResourceUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.InputStream;

import static io.payworks.labs.tcpmocker.test.TcpMappingsRegistry.TEST_DEFAULT_JSON_MAPPING_1;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class JsonDataHandlerFactoryTest {
    @Mock
    private DataHandlerModelFactory dataHandlerModelFactory;
    @InjectMocks
    private JsonDataHandlerFactory jsonDataHandlerFactory;

    @BeforeMethod
    public void setUp() {
        initMocks(this);
    }

    @AfterMethod
    public void tearDown() {
        jsonDataHandlerFactory = null;
    }

    @Test
    public void testCreateJsonDataHandler() {
        final InputStream inputStream = ResourceUtils.getInputStream(TEST_DEFAULT_JSON_MAPPING_1);
        final DataHandler expectedDataHandler = mock(DataHandler.class);
        given(dataHandlerModelFactory.createDataHandler(any()))
                .willReturn(expectedDataHandler);

        final DataHandler dataHandlerModel = jsonDataHandlerFactory.create(inputStream);

        assertThat(dataHandlerModel, equalTo(expectedDataHandler));
        verify(dataHandlerModelFactory).createDataHandler(any());
    }

}