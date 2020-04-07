package io.payworks.labs.tcpmocker.recording;

import org.apache.commons.lang3.RandomUtils;
import org.assertj.core.groups.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.payworks.labs.tcpmocker.test.pageable.PageableCollector.collectToList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

@DataJpaTest(showSql = false)
public class RecordingsDataJpaTest extends AbstractTestNGSpringContextTests {

    private static final int REQUEST_SIZE_BYTES = 100;
    private static final int REPLY_SIZE_BYTES = 50;
    private static final int PAGE_SIZE = 3;

    @Autowired
    private RecordingsRepository recordingsRepository;

    @BeforeMethod
    private void setup() {
        recordingsRepository.deleteAll();
    }

    @Test
    public void saveRecordingEntity() {
        final RecordingEntity givenRecordingEntity = createRandomRecordingEntity();

        final RecordingEntity recordingEntity = recordingsRepository.save(givenRecordingEntity);

        assertThat(recordingEntity.getId()).isNotNull();
        assertEntitiesAreEqual(recordingEntity, givenRecordingEntity);
    }

    @DataProvider
    public Object[][] length() {
        return new Object[][]{
                {0},
                {1},
                {10},
                {999}
        };
    }

    @Test(dataProvider = "length")
    public void getLastRecording(final int length) {
        final Deque<RecordingEntity> givenRecordingEntities = createRandomRecordingEntities(length);

        recordingsRepository.saveAll(givenRecordingEntities);

        final Optional<RecordingEntity> lastRecordingEntity = recordingsRepository.findTopByOrderByTimestampDesc();

        if (length == 0) {
            assertThat(lastRecordingEntity).isEmpty();
        } else {
            assertThat(lastRecordingEntity)
                    .hasValueSatisfying(recordingEntity ->
                            assertEntitiesAreEqual(recordingEntity, givenRecordingEntities.getLast()));
        }
    }

    @Test(dataProvider = "length")
    public void findAllWithPageRequest(final int length) {
        final Deque<RecordingEntity> givenRecordingEntities = createRandomRecordingEntities(length);

        recordingsRepository.saveAll(givenRecordingEntities);

        final List<RecordingEntity> recordingEntities =
                collectToList(recordingsRepository::findAllByOrderByTimestampDesc, PageRequest::of, PAGE_SIZE);

        assertThat(recordingEntities)
                .extracting(
                        RecordingEntity::getTimestamp,
                        RecordingEntity::getRequest,
                        RecordingEntity::getReply)
                .contains(
                        givenRecordingEntities.stream()
                                .map(entity -> tuple(
                                        entity.getTimestamp(),
                                        entity.getRequest(),
                                        entity.getReply()))
                                .toArray(Tuple[]::new));
    }

    private static void assertEntitiesAreEqual(final RecordingEntity actual,
                                               final RecordingEntity expected) {
        assertThat(actual)
                .extracting(
                        RecordingEntity::getTimestamp,
                        RecordingEntity::getRequest,
                        RecordingEntity::getReply)
                .contains(
                        expected.getTimestamp(),
                        expected.getRequest(),
                        expected.getReply());
    }

    private static RecordingEntity createRandomRecordingEntity() {
        final byte[] request = RandomUtils.nextBytes(REQUEST_SIZE_BYTES);
        final byte[] reply = RandomUtils.nextBytes(REPLY_SIZE_BYTES);

        return RecordingEntity.builder()
                .withRequest(request)
                .withReply(reply)
                .build();
    }

    private static Deque<RecordingEntity> createRandomRecordingEntities(final int length) {
        return Stream.generate(RecordingsDataJpaTest::createRandomRecordingEntity)
                .limit(length)
                .collect(Collectors.toCollection(ArrayDeque::new));
    }
}
