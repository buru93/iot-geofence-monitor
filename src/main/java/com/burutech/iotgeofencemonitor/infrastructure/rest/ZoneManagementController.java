package com.burutech.iotgeofencemonitor.infrastructure.rest;

import com.burutech.iotgeofencemonitor.application.ports.input.CreateZoneUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/zones")
@RequiredArgsConstructor
public class ZoneManagementController {

    private final CreateZoneUseCase createZoneUseCase;

    @PostMapping
    public ResponseEntity<Void> createZone(@Valid @RequestBody CreateZoneRequest request) {
        log.info("Received request to create zone: {}", request.getName());

        createZoneUseCase.createCircularZone(
                request.getName(),
                request.getLatitude(),
                request.getLongitude(),
                request.getRadius());

        log.info("Zone '{}' created successfully", request.getName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
