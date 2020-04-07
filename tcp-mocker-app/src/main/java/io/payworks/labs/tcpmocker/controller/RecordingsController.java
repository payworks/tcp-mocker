package io.payworks.labs.tcpmocker.controller;

import io.payworks.labs.tcpmocker.recording.RecordingData;
import io.payworks.labs.tcpmocker.recording.RecordingsService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/recordings", produces = MediaType.APPLICATION_JSON_VALUE)
public class RecordingsController {

    private final RecordingsService recordingsService;

    public RecordingsController(final RecordingsService recordingsService) {
        this.recordingsService = recordingsService;
    }

    @GetMapping(path = "last")
    public ResponseEntity<RecordingData> getLastRecording() {
        return recordingsService.getLastRecording()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(params = {"page", "size"})
    public List<RecordingData> getRecordings(@RequestParam("page") int page,
                                             @RequestParam("size") int size) {
        return recordingsService.listRecordings(page, size);
    }
}
