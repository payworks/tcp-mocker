package io.payworks.labs.tcpmocker.support.groovy;

import io.payworks.labs.tcpmocker.datahandler.DataHandler;
import io.payworks.labs.tcpmocker.support.resource.ResourceUtils;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.util.Optional;

import static com.spotify.hamcrest.optional.OptionalMatchers.optionalWithValue;
import static io.payworks.labs.tcpmocker.test.TcpMappingsRegistry.TEST_GROOVY_DATA_HANDLER;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class GroovyDataHandlerFactoryTest {

    private final GroovyDataHandlerFactory groovyDataHandlerFactory = new GroovyDataHandlerFactory();

    @Test
    public void testCreate() {
        final InputStream inputStream = ResourceUtils.getInputStream(TEST_GROOVY_DATA_HANDLER);
        final DataHandler dataHandler = groovyDataHandlerFactory.create(inputStream);

        final byte[] request = new byte[]{0x02, 'p', 'i', 'n', 'g', 0x03};

        final Optional<byte[]> response = dataHandler.handle(request);
        assertThat(response, is(optionalWithValue(equalTo(new byte[]{0x02, 'p', 'o', 'n', 'g', 0x03}))));
    }
}