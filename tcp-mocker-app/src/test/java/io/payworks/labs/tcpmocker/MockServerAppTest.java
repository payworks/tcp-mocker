package io.payworks.labs.tcpmocker;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import io.payworks.labs.tcpmocker.properties.MockServerProperties;
import io.payworks.labs.tcpmocker.recording.RecordingData;
import io.payworks.labs.tcpmocker.test.pageable.PageableCollector;
import io.payworks.labs.tcpmocker.test.tcpclient.SimpleTcpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.web.util.UriComponentsBuilder;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import static com.google.common.io.BaseEncoding.base16;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@DirtiesContext
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MockServerAppTest extends AbstractTestNGSpringContextTests {

    public static final long TEST_TIMEOUT = 5000L;

    private static final String REQUEST_PAYLOAD = "70696E67"; // ping
    private static final String REPLY_PAYLOAD = "706F6E67"; // pong

    @Autowired
    private TestRestTemplate restTemplate; // TODO: change to WebTestClient

    @Autowired
    private MockServerProperties mockServerProperties;

    private SimpleTcpClient tcpClient;

    @BeforeMethod
    public void setup() throws Exception {
        tcpClient = new SimpleTcpClient("localhost", mockServerProperties.getPort());
    }

    @AfterMethod
    public void close() throws Exception {
        tcpClient.close();
    }

    @Test(timeOut = TEST_TIMEOUT)
    public void requestIsHandledAndRecorded() {
        final String reply = whenSendRequestPayload();

        assertThat(reply).isEqualTo(REPLY_PAYLOAD);

        final ResponseEntity<RecordingData> response = whenQueryLastRecording();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
                .extracting(
                        RecordingData::getRequest,
                        RecordingData::getReply)
                .contains(
                        REQUEST_PAYLOAD,
                        reply);
    }

    @Test
    public void getAllRecordingsUsingRecordingsApi() {
        whenSendRequestPayloadTimes(100);

        final List<RecordingData> recordingDataList =
                PageableCollector.collectToList(this::whenQueryingForPageAndSize, 13);

        assertThat(recordingDataList)
                .isNotEmpty()
                .extracting(
                        RecordingData::getRequest,
                        RecordingData::getReply)
                .contains(
                        tuple(REQUEST_PAYLOAD, REPLY_PAYLOAD));
    }

    @Test
    public void mayReceiveEmptyReply() throws IOException {
        final String reply = base16().encode(
                tcpClient.sendAndReceive(
                        base16().decode("70696E6800")));

        assertThat(reply).isEmpty();
    }

    private ResponseEntity<RecordingData> whenQueryLastRecording() {
        return restTemplate.getForEntity(
                UriComponentsBuilder.fromUriString(restTemplate.getRootUri())
                        .path("/recordings/last")
                        .build()
                        .toUri(), RecordingData.class);
    }

    private List<RecordingData> whenQueryingForPageAndSize(final int pageNumber, final int pageSize) {
        return restTemplate.exchange(
                UriComponentsBuilder.fromUriString(restTemplate.getRootUri())
                        .path("recordings")
                        .queryParam("page", pageNumber)
                        .queryParam("size", pageSize)
                        .build()
                        .toUri(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<RecordingData>>() {})
                .getBody();
    }

    @CanIgnoreReturnValue
    private long whenSendRequestPayloadTimes(final int times) {
        return Stream.generate(this::whenSendRequestPayload)
                .limit(times)
                .count();
    }

    private String whenSendRequestPayload() throws RuntimeException {
        try {
            return base16().encode(tcpClient.sendAndReceive(base16().decode(REQUEST_PAYLOAD)));
        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
