package io.payworks.labs.tcpmocker.controller;

import io.payworks.labs.tcpmocker.controller.resources.ResponseDelayResource;
import io.payworks.labs.tcpmocker.controller.resources.ResponseShreddingResource;
import io.payworks.labs.tcpmocker.properties.TcpMockerProperties;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/responseshredding", produces = MediaType.APPLICATION_JSON_VALUE)
public class ShredResponseController {
    private final TcpMockerProperties properties;

    public ShredResponseController(final TcpMockerProperties properties) {
        this.properties = properties;
    }

    @GetMapping
    public ResponseEntity<ResponseShreddingResource> getResponseShredding() {
        return ResponseEntity.ok(new ResponseShreddingResource(properties.isShredResponses()));
    }

    @PostMapping
    public ResponseEntity<ResponseShreddingResource> setResponseShredding(@RequestBody final ResponseShreddingResource responseShredding) {
        properties.setShredResponses(responseShredding.isEnabled());
        return ResponseEntity.ok(new ResponseShreddingResource(properties.isShredResponses()));
    }
}
