package io.payworks.labs.tcpmocker.recording;

import com.google.common.collect.ImmutableList;
import io.payworks.labs.tcpmocker.test.pageable.PageableCollector;
import org.apache.commons.lang3.RandomUtils;
import org.assertj.core.groups.Tuple;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Pageable;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.io.BaseEncoding.base16;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;

public class RecordingsServiceTest {

    private static final int TESTING_PAGE_SIZE = 3;

    @Mock
    private RecordingsRepository recordingsRepository;

    @InjectMocks
    private RecordingsService victim;

    @BeforeMethod
    void setup() {
        initMocks(this);
    }

    @AfterMethod
    void close() {
        victim = null;
    }

    @Test
    public void getLastRecordingQueriesTheRepositoryForTheLastRecording() {
        final RecordingEntity givenRecordingEntity = givenRecordingEntity();

        given(recordingsRepository.findTopByOrderByTimestampDesc())
                .willReturn(Optional.of(givenRecordingEntity));

        final Optional<RecordingData> optionalLastRecording = victim.getLastRecording();

        assertThat(optionalLastRecording)
                .hasValueSatisfying(lastRecording -> assertThat(lastRecording)
                        .extracting(
                                RecordingData::getTimestamp,
                                RecordingData::getRequest,
                                RecordingData::getReply)
                        .contains(
                                givenRecordingEntity.getTimestamp(),
                                base16().encode(givenRecordingEntity.getRequest()),
                                base16().encode(givenRecordingEntity.getReply())));
    }

    private static RecordingEntity givenRecordingEntity() {
        return RecordingEntity.builder()
                .withRequest(RandomUtils.nextBytes(100))
                .withReply(RandomUtils.nextBytes(10))
                .build();
    }

    @Test
    public void getLastRecordingsReturnsEmptyIfRepositoryIsEmpty() {
        given(recordingsRepository.findTopByOrderByTimestampDesc())
                .willReturn(Optional.empty());

        final Optional<RecordingData> recordingData = victim.getLastRecording();

        assertThat(recordingData).isEmpty();
    }

    @Test
    public void getAllRecordingsQueryCollectsThemFromTheRepository() {
        final List<RecordingEntity> page1 = givenRecordingEntities(10);
        final List<RecordingEntity> page2 = givenRecordingEntities(10);
        final List<RecordingEntity> page3 = givenRecordingEntities(10);
        final List<RecordingEntity> page4 = ImmutableList.of();

        given(recordingsRepository.findAllByOrderByTimestampDesc(any(Pageable.class)))
                .willReturn(page1, page2, page3, page4);

        final List<RecordingData> recordingDataList =
                PageableCollector.collectToList(victim::listRecordings, TESTING_PAGE_SIZE);

        assertThatAllRecordingEntitiesCollected(recordingDataList, page1, page2, page3);
    }

    private static List<RecordingEntity> givenRecordingEntities(final int length) {
        return Stream.generate(RecordingsServiceTest::givenRecordingEntity)
                .limit(length)
                .collect(Collectors.toList());
    }

    private static void assertThatAllRecordingEntitiesCollected(final List<RecordingData> recordingDataList,
                                                                final List<RecordingEntity>... pages) {
        assertThat(recordingDataList)
                .isNotEmpty()
                .extracting(
                        RecordingData::getTimestamp,
                        RecordingData::getRequest,
                        RecordingData::getReply)
                .contains(
                        Arrays.stream(pages).flatMap(Collection::stream)
                                .collect(Collectors.toList())
                                .stream()
                                .map(entity -> tuple(
                                        entity.getTimestamp(),
                                        base16().encode(entity.getRequest()),
                                        base16().encode(entity.getReply())))
                                .toArray(Tuple[]::new));

    }

    @Test
    public void listRecordingsReturnsAnEmptyListIfTheRepositoryIsEmpty() {
        given(recordingsRepository.findAll())
                .willReturn(ImmutableList.of());

        final List<RecordingData> recordingDataList =
                PageableCollector.collectToList(victim::listRecordings, TESTING_PAGE_SIZE);

        assertThat(recordingDataList).isEmpty();
    }
}