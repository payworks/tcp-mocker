package io.payworks.labs.tcpmocker.recording;

import io.payworks.labs.tcpmocker.datahandler.DataHandler;
import org.apache.commons.lang3.RandomUtils;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.MockitoAnnotations.initMocks;

public class RecordingDataHandlerTest {

    @Mock
    private RecordingsRepository recordingsRepository;

    @Mock
    private DataHandler delegateDataHandler;

    @InjectMocks
    private RecordingDataHandler victim;

    @BeforeMethod
    public void setup() {
        initMocks(this);
    }

    @AfterMethod
    public void close() {
        this.victim = null;
    }

    @Test
    public void handleSavesRecordingIfDelegateSuccessfullyHandlesGivenData() {
        final byte[] givenRequest = RandomUtils.nextBytes(100);
        final byte[] givenReply = RandomUtils.nextBytes(50);

        final RecordingEntity givenRecordingEntity = RecordingEntity.builder()
                .withRequest(givenRequest)
                .withReply(givenReply)
                .build();

        given(delegateDataHandler.handle(any()))
                .willReturn(Optional.of(givenReply));
        given(recordingsRepository.save(any()))
                .willReturn(givenRecordingEntity);

        final Optional<byte[]> reply = victim.handle(givenRequest);

        assertThat(reply).hasValue(givenReply);
        assertRecordingEntitySaved(givenRequest, givenReply);
    }

    private void assertRecordingEntitySaved(final byte[] givenRequest,
                                            final byte[] givenReply) {
        final ArgumentCaptor<RecordingEntity> recordingEntityArgumentCaptor =
                ArgumentCaptor.forClass(RecordingEntity.class);

        then(recordingsRepository).should()
                .save(recordingEntityArgumentCaptor.capture());

        assertThat(recordingEntityArgumentCaptor.getValue())
                .extracting(
                        RecordingEntity::getRequest,
                        RecordingEntity::getReply)
                .contains(
                        givenRequest,
                        givenReply);
    }

    @Test
    public void handleDoesntSaveRecordingIfDelegateDataHandlerFailsToHandleGivenData() {
        final byte[] givenRequest = RandomUtils.nextBytes(100);
        given(delegateDataHandler.handle(any()))
                .willReturn(Optional.empty());

        final Optional<byte[]> reply = victim.handle(givenRequest);

        assertThat(reply).isEmpty();

        then(recordingsRepository).shouldHaveZeroInteractions();
    }
}