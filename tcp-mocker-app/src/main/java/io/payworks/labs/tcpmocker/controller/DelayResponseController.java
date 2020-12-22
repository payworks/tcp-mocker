package io.payworks.labs.tcpmocker.controller;

import io.payworks.labs.tcpmocker.controller.resources.ResponseDelayResource;
import io.payworks.labs.tcpmocker.properties.TcpMockerProperties;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/responsedelay", produces = MediaType.APPLICATION_JSON_VALUE)
public class DelayResponseController {
    private final TcpMockerProperties properties;

    public DelayResponseController(final TcpMockerProperties properties) {
        this.properties = properties;
    }

    @GetMapping
    public ResponseEntity<ResponseDelayResource> getResponseDelay() {
        return ResponseEntity.ok(new ResponseDelayResource(properties.getResponseDelay()));
    }

    @PostMapping
    public ResponseEntity<ResponseDelayResource> setResponseDelay(@RequestBody @Valid final ResponseDelayResource responseDelay) {
        properties.setResponseDelay(responseDelay.getDuration());
        return ResponseEntity.ok(new ResponseDelayResource(properties.getResponseDelay()));
    }
}
