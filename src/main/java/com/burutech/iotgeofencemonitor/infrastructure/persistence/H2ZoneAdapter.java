package com.burutech.iotgeofencemonitor.infrastructure.persistence;

import com.burutech.iotgeofencemonitor.application.ports.output.LoadZonesPort;
import com.burutech.iotgeofencemonitor.application.ports.output.SaveZonePort;
import com.burutech.iotgeofencemonitor.domain.model.CircularZone;
import com.burutech.iotgeofencemonitor.domain.model.Location;
import com.burutech.iotgeofencemonitor.domain.model.Zone;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class H2ZoneAdapter implements LoadZonesPort, SaveZonePort {

    private final SpringDataH2ZoneRepository repository;

    @Override
    public List<Zone> getAllActiveZones() {
        return repository.findAll().stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void save(Zone zone) {
        if (zone instanceof CircularZone circularZone) {
            H2ZoneEntity entity = mapToEntity(circularZone);
            repository.save(entity);
        } else {
            throw new UnsupportedOperationException("Only CircularZone is supported for persistence");
        }
    }

    private Zone mapToDomain(H2ZoneEntity entity) {
        return new CircularZone(
                entity.getId(),
                entity.getName(),
                new Location(entity.getLatitude(), entity.getLongitude()),
                entity.getRadius());
    }

    private H2ZoneEntity mapToEntity(CircularZone zone) {
        H2ZoneEntity entity = new H2ZoneEntity();
        entity.setId(zone.getId());
        entity.setName(zone.getName());
        entity.setLatitude(zone.getCenter().latitude());
        entity.setLongitude(zone.getCenter().longitude());
        entity.setRadius(zone.getRadius());
        return entity;
    }
}
