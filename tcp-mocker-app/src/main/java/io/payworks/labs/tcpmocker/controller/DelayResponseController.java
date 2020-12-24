package io.payworks.labs.tcpmocker.controller;

import io.payworks.labs.tcpmocker.controller.model.ResponseDelayModel;
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
    public ResponseEntity<ResponseDelayModel> getResponseDelay() {
        return ResponseEntity.ok(new ResponseDelayModel(properties.getResponseDelay()));
    }

    @PostMapping
    public ResponseEntity<ResponseDelayModel> setResponseDelay(@RequestBody @Valid final ResponseDelayModel responseDelay) {
        properties.setResponseDelay(responseDelay.getDuration());
        return ResponseEntity.ok(new ResponseDelayModel(properties.getResponseDelay()));
    }
}
