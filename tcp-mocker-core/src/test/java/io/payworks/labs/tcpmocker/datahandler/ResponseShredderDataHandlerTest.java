package io.payworks.labs.tcpmocker.datahandler;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Optional;
import java.util.function.Supplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.MockitoAnnotations.initMocks;

public class ResponseShredderDataHandlerTest {
    @Mock
    private Supplier<Boolean> shredResponseSupplier;

    @Mock
    private DataHandler delegate;

    @InjectMocks
    private ResponseShredderDataHandler dataHandler;

    @BeforeMethod
    public void setup() {
        initMocks(this);
        given(shredResponseSupplier.get()).willReturn(false);
    }

    @AfterMethod
    public void teardown() {
        dataHandler = null;
    }

    @Test
    public void handlingDataIsDelegated() {
        final var givenRequestData = new byte[]{};

        dataHandler.handle(givenRequestData);

        then(delegate).should().handle(givenRequestData);
    }

    @Test
    public void delegateResponseIsReturnedIfNotConfiguredToShredResponses() {
        final Optional<byte[]> givenResponse = Optional.of(new byte[]{});
        given(delegate.handle(any())).willReturn(givenResponse);
        given(shredResponseSupplier.get()).willReturn(false);

        final var actualResponse = dataHandler.handle(new byte[]{});

        assertThat(actualResponse, is(givenResponse));
    }

    @Test
    public void returnValueIsEmptyIfConfiguredToShredResponses() {
        given(delegate.handle(any())).willReturn(Optional.of(new byte[]{}));
        given(shredResponseSupplier.get()).willReturn(true);

        final var actualResponse = dataHandler.handle(new byte[]{});

        assertThat(actualResponse, is(Optional.empty()));
    }
}