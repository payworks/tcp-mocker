package io.payworks.labs.tcpmocker.recording;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecordingsRepository extends PagingAndSortingRepository<RecordingEntity, Long> {

    Optional<RecordingEntity> findTopByOrderByTimestampDesc();

    List<RecordingEntity> findAllByOrderByTimestampDesc(Pageable pageable);
}
