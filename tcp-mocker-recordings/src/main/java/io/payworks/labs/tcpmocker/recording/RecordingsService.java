package io.payworks.labs.tcpmocker.recording;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecordingsService {

    private final RecordingsRepository recordingsRepository;

    public RecordingsService(final RecordingsRepository recordingsRepository) {
        this.recordingsRepository = recordingsRepository;
    }

    public Optional<RecordingData> getLastRecording() {
        return recordingsRepository.findTopByOrderByTimestampDesc()
                .map(RecordingData::fromEntity);
    }

    public List<RecordingData> listRecordings(final int pageNumber, final int size) {
        return recordingsRepository.findAllByOrderByTimestampDesc(PageRequest.of(pageNumber, size))
                .stream()
                .map(RecordingData::fromEntity)
                .collect(Collectors.toList());
    }
}
