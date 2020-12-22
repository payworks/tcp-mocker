package io.payworks.labs.tcpmocker.datahandler;


import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.MockitoAnnotations.initMocks;

public class DelayedResponseDataHandlerTest {

    @Mock
    private Supplier<Duration> responseDelaySupplier;

    @Mock
    private DataHandler delegate;

    @Mock
    private Consumer<Duration> delayer;

    @InjectMocks
    private DelayedResponseDataHandler dataHandler;

    @BeforeMethod
    public void setup() {
        initMocks(this);
        given(responseDelaySupplier.get()).willReturn(Duration.ZERO);
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
    public void returnValueIsResponseDataProducedByDelegate() {
        final Optional<byte[]> givenResponse = Optional.of(new byte[]{});
        given(delegate.handle(any())).willReturn(givenResponse);

        final var actualResponse = dataHandler.handle(new byte[]{});

        assertThat(actualResponse, is(givenResponse));
    }

    @Test
    public void noDelayIsAppliedIfResponseIsEmpty() {
        final Optional<byte[]> givenResponse = Optional.empty();
        given(delegate.handle(any())).willReturn(givenResponse);

        dataHandler.handle(new byte[]{});

        then(responseDelaySupplier).shouldHaveNoInteractions();
    }

    @Test
    @SuppressWarnings("ReturnValueIgnored")
    public void delayIsCollectedWhenThereIsAResponse() {
        final Optional<byte[]> givenResponse = Optional.of(new byte[]{});
        given(delegate.handle(any())).willReturn(givenResponse);

        dataHandler.handle(new byte[]{});

        then(responseDelaySupplier).should().get();
    }

    @Test
    public void zeroValueDelayIsSkipped() {
        final Optional<byte[]> givenResponse = Optional.of(new byte[]{});
        given(delegate.handle(any())).willReturn(givenResponse);
        given(responseDelaySupplier.get()).willReturn(Duration.ZERO);

        dataHandler.handle(new byte[]{});

        then(delayer).shouldHaveNoInteractions();
    }

    @Test
    public void positiveValueDelayIsAppliedWhenThereIsAResponse() {
        final Optional<byte[]> givenResponse = Optional.of(new byte[]{});
        given(delegate.handle(any())).willReturn(givenResponse);
        final Duration givenDelay = Duration.ofMillis(1L);
        given(responseDelaySupplier.get()).willReturn(givenDelay);

        dataHandler.handle(new byte[]{});

        then(delayer).should().accept(givenDelay);
    }
}