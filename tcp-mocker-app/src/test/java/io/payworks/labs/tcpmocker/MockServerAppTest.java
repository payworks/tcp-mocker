package io.payworks.labs.tcpmocker;

import io.payworks.labs.tcpmocker.data.RecordingData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.web.util.UriComponentsBuilder;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.util.List;
import java.util.stream.Stream;

import static com.google.common.io.BaseEncoding.base16;
import static io.payworks.labs.tcpmocker.util.PageCollectorTemplate.collectAllPages;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MockServerAppTest extends AbstractTestNGSpringContextTests {

    private static final String REQUEST_PAYLOAD = "70696E67"; // ping
    private static final String REPLY_PAYLOAD = "706F6E67"; // pong

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MockServerProperties mockServerProperties;

    private SimpleTcpClient tcpClient;

    @BeforeMethod
    public void setUp() throws Exception {
        tcpClient = new SimpleTcpClient("localhost", mockServerProperties.getPort());
    }

    @AfterMethod
    public void tearDown() throws Exception {
        tcpClient.close();
    }

    @Test(timeOut = 5000L)
    public void matchingRequestIsServed() {
        final String reply = whenSendingExpectedRequest();

        assertThat(reply).isEqualTo(REPLY_PAYLOAD);
    }

    @Test(timeOut = 5000L)
    public void requestIsRecorded() {
        final String reply = whenSendingExpectedRequest();

        final ResponseEntity<RecordingData> response = whenQueryingForLastRecording();

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
    public void canRetrieveAllRecordingsUsingPages() {
        whenSendingExpectedRequestTimes(100);

        final List<RecordingData> recordingData = collectAllPages(this::whenQueryingForPageAndSize, 13);

        assertThat(recordingData)
                .isNotEmpty()
                .extracting(
                        RecordingData::getRequest,
                        RecordingData::getReply)
                .contains(
                        tuple(REQUEST_PAYLOAD, REPLY_PAYLOAD));
    }

    @Test()
    public void canReturnEmptyReply() throws IOException {
        String reply = base16().encode(
                tcpClient.sendAndReceive(
                        base16().decode("70696E6800")));

        assertThat(reply).isEmpty();
    }

    private ResponseEntity<RecordingData> whenQueryingForLastRecording() {
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
                new ParameterizedTypeReference<List<RecordingData>>() {
                })
                .getBody();
    }

    private void whenSendingExpectedRequestTimes(final int times) {
        Stream.generate(this::whenSendingExpectedRequest).limit(times).count();
    }

    private String whenSendingExpectedRequest() throws RuntimeException {
        try {
            return base16().encode(
                    tcpClient.sendAndReceive(
                            base16().decode(REQUEST_PAYLOAD)));
        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
