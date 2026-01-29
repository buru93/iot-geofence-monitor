package com.burutech.iotgeofencemonitor.infrastructure.rest;

import com.burutech.iotgeofencemonitor.application.ports.input.MonitorDeviceUseCase;
import com.burutech.iotgeofencemonitor.domain.model.DeviceSignal;
import com.burutech.iotgeofencemonitor.domain.model.Location;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/signals")
@RequiredArgsConstructor
public class DeviceSignalController {

    private final MonitorDeviceUseCase monitorDeviceUseCase;

    @PostMapping
    public ResponseEntity<Void> receiveSignal(@Valid @RequestBody SignalRequest request) {
        DeviceSignal signal = new DeviceSignal(
                request.getDeviceId(),
                new Location(request.getLatitude(), request.getLongitude()),
                request.getTimestamp());

        monitorDeviceUseCase.processSignal(signal);

        return ResponseEntity.ok().build();
    }
}
