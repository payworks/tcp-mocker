package io.payworks.labs.tcpmocker.controller;

import io.payworks.labs.tcpmocker.controller.model.ResponseShreddingModel;
import io.payworks.labs.tcpmocker.properties.TcpMockerProperties;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/responseshredding", produces = MediaType.APPLICATION_JSON_VALUE)
public class ShredResponseController {
    private final TcpMockerProperties properties;

    public ShredResponseController(final TcpMockerProperties properties) {
        this.properties = properties;
    }

    @GetMapping
    public ResponseEntity<ResponseShreddingModel> getResponseShredding() {
        return ResponseEntity.ok(new ResponseShreddingModel(properties.isShredResponses()));
    }

    @PostMapping
    public ResponseEntity<ResponseShreddingModel> setResponseShredding(@RequestBody final ResponseShreddingModel responseShredding) {
        properties.setShredResponses(responseShredding.isEnabled());
        return ResponseEntity.ok(new ResponseShreddingModel(properties.isShredResponses()));
    }
}
