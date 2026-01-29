package com.burutech.iotgeofencemonitor.application.service;

import com.burutech.iotgeofencemonitor.application.ports.input.MonitorDeviceUseCase;
import com.burutech.iotgeofencemonitor.application.ports.output.DeviceOwnerRepositoryPort;
import com.burutech.iotgeofencemonitor.application.ports.output.LoadZonesPort;
import com.burutech.iotgeofencemonitor.application.ports.output.NotificationPort;
import com.burutech.iotgeofencemonitor.application.ports.output.SaveAlertPort;
import com.burutech.iotgeofencemonitor.domain.model.Alert;
import com.burutech.iotgeofencemonitor.domain.model.DeviceOwner;
import com.burutech.iotgeofencemonitor.domain.model.DeviceSignal;
import com.burutech.iotgeofencemonitor.domain.model.Location;
import com.burutech.iotgeofencemonitor.domain.model.Zone;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeofenceMonitorService implements MonitorDeviceUseCase {

    private final LoadZonesPort loadZonesPort;
    private final SaveAlertPort saveAlertPort;
    private final DeviceOwnerRepositoryPort deviceOwnerRepositoryPort;
    private final NotificationPort notificationPort;

    @Override
    public void processSignal(DeviceSignal signal) {
        Location location = signal.location();

        loadZonesPort.getAllActiveZones().stream()
                .filter(zone -> !zone.isInside(location))
                .forEach(zone -> handleOutsideZone(signal, zone));
    }

    private void handleOutsideZone(DeviceSignal signal, Zone zone) {
        log.info("Checking breach for device {} in zone {}", signal.deviceId(), zone.getId());

        // 1. Buscamos al dueño de forma segura
        Optional<DeviceOwner> ownerOpt = deviceOwnerRepositoryPort.findOwnerByDeviceId(signal.deviceId());

        if (ownerOpt.isEmpty()) {
            log.error("SKIPPING ALERT: No owner found for device {}. Signal: {}", signal.deviceId(), signal);
            return; // Salimos de este procesamiento pero el Stream continúa con la siguiente zona
        }

        // 2. Si el dueño existe, procedemos con la alerta
        DeviceOwner owner = ownerOpt.get();

        Alert alert = Alert.builder()
                .id(UUID.randomUUID())
                .signal(signal)
                .zoneId(zone.getId())
                .message(String.format("Device %s exited zone %s", signal.deviceId(), zone.getName()))
                .owner(owner)
                .build();

        try {
            saveAlertPort.save(alert);
            notificationPort.notify(alert);
        } catch (Exception e) {
            // Manejo de errores en la persistencia/notificación para no bloquear el flujo
            log.error("Failed to process alert for device {} in zone {}: {}", signal.deviceId(), zone.getId(),
                    e.getMessage());
        }
    }
}
