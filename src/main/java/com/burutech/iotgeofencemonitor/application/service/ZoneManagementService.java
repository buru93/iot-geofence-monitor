package com.burutech.iotgeofencemonitor.application.service;

import com.burutech.iotgeofencemonitor.application.ports.input.CreateZoneUseCase;
import com.burutech.iotgeofencemonitor.application.ports.output.SaveZonePort;
import com.burutech.iotgeofencemonitor.domain.model.CircularZone;
import com.burutech.iotgeofencemonitor.domain.model.Location;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ZoneManagementService implements CreateZoneUseCase {

    private final SaveZonePort saveZonePort;

    @Override
    public void createCircularZone(String name, double lat, double lon, double radius) {
        log.info("Creating new zone '{}' at [{}, {}] with radius {}m", name, lat, lon, radius);

        Location center = new Location(lat, lon);
        CircularZone zone = new CircularZone(UUID.randomUUID().toString(), name, center, radius);

        saveZonePort.save(zone);
        log.info("Zone '{}' created with ID: {}", name, zone.getId());
    }
}
