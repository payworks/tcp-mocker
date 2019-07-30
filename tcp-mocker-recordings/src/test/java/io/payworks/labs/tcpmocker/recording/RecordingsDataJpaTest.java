package io.payworks.labs.tcpmocker.recording;

import org.apache.commons.lang3.RandomUtils;
import org.assertj.core.groups.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
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

@DataJpaTest
public class RecordingsDataJpaTest extends AbstractTestNGSpringContextTests {

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
        assertEntitiesEqual(recordingEntity, givenRecordingEntity);
    }

    private static RecordingEntity createRandomRecordingEntity() {
        final byte[] request = RandomUtils.nextBytes(100);
        final byte[] reply = RandomUtils.nextBytes(50);

        try {
            Thread.sleep(100);
        } catch (final InterruptedException ignored) {
        }

        return RecordingEntity.builder()
                .withRequest(request)
                .withReply(reply)
                .build();
    }

    @Test
    public void getLastRecording() {
        final Deque<RecordingEntity> givenRecordingEntities = createRandomRecordingEntities(10);

        recordingsRepository.saveAll(givenRecordingEntities);

        final Optional<RecordingEntity> lastRecordingEntity = recordingsRepository.findTopByOrderByTimestampDesc();

        assertThat(lastRecordingEntity)
                .hasValueSatisfying(recordingEntity ->
                        assertEntitiesEqual(recordingEntity, givenRecordingEntities.getLast()));
    }

    private static Deque<RecordingEntity> createRandomRecordingEntities(final int length) {
        return Stream.generate(RecordingsDataJpaTest::createRandomRecordingEntity)
                .limit(length)
                .collect(Collectors.toCollection(ArrayDeque::new));
    }

    private void assertEntitiesEqual(final RecordingEntity actual,
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

    @Test
    public void findAllWithPageRequest() {
        final Deque<RecordingEntity> givenRecordingEntities = createRandomRecordingEntities(10);

        recordingsRepository.saveAll(givenRecordingEntities);

        final List<RecordingEntity> recordingEntities =
                collectToList(recordingsRepository::findAllByOrderByTimestampDesc, PageRequest::of, 3);

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
}
