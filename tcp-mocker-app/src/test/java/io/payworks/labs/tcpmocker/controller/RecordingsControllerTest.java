package io.payworks.labs.tcpmocker.controller;

import com.google.common.collect.ImmutableList;
import io.payworks.labs.tcpmocker.recording.RecordingData;
import io.payworks.labs.tcpmocker.recording.RecordingsService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testng.annotations.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecordingsController.class)
public class RecordingsControllerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    @MockBean
    private RecordingsService recordingsService;

    @Test
    public void getLastRecordingReturnsTheValueProvidedByTheService() throws Exception {
        final RecordingData givenRecordingData = givenRecordingData();
        given(recordingsService.getLastRecording()).willReturn(Optional.of(givenRecordingData));

        this.mvc.perform(
                get("/recordings/last").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.timestamp", is(givenRecordingData.getTimestamp().toString())))
                .andExpect(jsonPath("$.request", is(givenRecordingData.getRequest())))
                .andExpect(jsonPath("$.reply", is(givenRecordingData.getReply())));
    }

    private RecordingData givenRecordingData() {
        return RecordingData.create()
                .withRequest(RandomStringUtils.randomAlphanumeric(100))
                .withReply(RandomStringUtils.randomAlphanumeric(100))
                .withTimestamp(Instant.now())
                .build();
    }

    @Test
    public void getLastReturnsNotFoundIfNoRecordingIsAvailable() throws Exception {
        given(recordingsService.getLastRecording()).willReturn(Optional.empty());

        this.mvc.perform(
                get("/recordings/last").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    public void queryingByPageUsesTheParametersOnTheService() throws Exception {
        final List<RecordingData> givenRecordingData = givenSomeRecordingData(10);
        given(recordingsService.listRecordings(5, 10)).willReturn(givenRecordingData);

        final ResultActions resultActions = this.mvc.perform(
                get("/recordings?page=5&size=10").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(10)));

        thenAllRecordingsHaveBeenReceived(givenRecordingData, resultActions);
    }

    private List<RecordingData> givenSomeRecordingData(final int length) {
        return Stream.generate(this::givenRecordingData).limit(length).collect(Collectors.toList());
    }

    private void thenAllRecordingsHaveBeenReceived(
            final List<RecordingData> givenRecordingData,
            final ResultActions resultActions) throws Exception {
        for (int ii = 0; ii < 10; ++ii) {
            thenNthRecordingHasBeenReceived(resultActions, ii, givenRecordingData.get(ii));
        }
    }

    private void thenNthRecordingHasBeenReceived(
            final ResultActions resultActions,
            final int index,
            final RecordingData givenRecordingData) throws Exception {
        resultActions
                .andExpect(jsonPath("$[" + index + "].timestamp", is(givenRecordingData.getTimestamp().toString())))
                .andExpect(jsonPath("$[" + index + "].request", is(givenRecordingData.getRequest())))
                .andExpect(jsonPath("$[" + index + "].reply", is(givenRecordingData.getReply())));
    }

    @Test
    public void queryByPageReturnsEmptyListIfNoRecordingIsAvailable() throws Exception {
        given(recordingsService.listRecordings(anyInt(), anyInt())).willReturn(ImmutableList.of());

        this.mvc.perform(
                get("/recordings?page=0&size=10").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}