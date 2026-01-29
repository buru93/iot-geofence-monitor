package com.burutech.iotgeofencemonitor.application.service;

import com.burutech.iotgeofencemonitor.application.ports.output.DeviceOwnerRepositoryPort;
import com.burutech.iotgeofencemonitor.application.ports.output.LoadZonesPort;
import com.burutech.iotgeofencemonitor.application.ports.output.NotificationPort;
import com.burutech.iotgeofencemonitor.application.ports.output.SaveAlertPort;
import com.burutech.iotgeofencemonitor.domain.model.Alert;
import com.burutech.iotgeofencemonitor.domain.model.CircularZone;
import com.burutech.iotgeofencemonitor.domain.model.DeviceOwner;
import com.burutech.iotgeofencemonitor.domain.model.DeviceSignal;
import com.burutech.iotgeofencemonitor.domain.model.Location;
import com.burutech.iotgeofencemonitor.domain.model.Zone;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GeofenceMonitorServiceTest {

    @Mock
    private LoadZonesPort loadZonesPort;

    @Mock
    private SaveAlertPort saveAlertPort;

    @Mock
    private DeviceOwnerRepositoryPort deviceOwnerRepositoryPort;

    @Mock
    private NotificationPort notificationPort;

    @InjectMocks
    private GeofenceMonitorService geofenceMonitorService;

    @Test
    void shouldProcessSignal_andDoNothing_whenInsideZone() {
        // Arrange
        Location zoneCenter = new Location(40.416775, -3.703790); // Madrid
        CircularZone zone = new CircularZone("zone-1", "Madrid Center", zoneCenter, 1000.0);

        // Signal inside the 1km radius
        Location signalLocation = new Location(40.416775, -3.703790);
        DeviceSignal signal = new DeviceSignal("device-123", signalLocation, Instant.now());

        when(loadZonesPort.getAllActiveZones()).thenReturn(List.of(zone));

        // Act
        geofenceMonitorService.processSignal(signal);

        // Assert
        verify(saveAlertPort, never()).save(any());
        verify(notificationPort, never()).notify(any());
    }

    @Test
    void shouldProcessSignal_andCreateAlert_whenOutsideZone() {
        // Arrange
        Location zoneCenter = new Location(40.416775, -3.703790); // Madrid
        CircularZone zone = new CircularZone("zone-1", "Madrid Center", zoneCenter, 1000.0);

        // Signal FAR outside (e.g. Barcelona)
        Location signalLocation = new Location(41.3851, 2.1734);
        DeviceSignal signal = new DeviceSignal("device-123", signalLocation, Instant.now());
        DeviceOwner owner = new DeviceOwner("owner-1", "user@example.com", "John Doe");

        when(loadZonesPort.getAllActiveZones()).thenReturn(List.of(zone));
        when(deviceOwnerRepositoryPort.findOwnerByDeviceId("device-123")).thenReturn(Optional.of(owner));

        // Act
        geofenceMonitorService.processSignal(signal);

        // Assert
        verify(saveAlertPort, times(1)).save(any(Alert.class));
        verify(notificationPort, times(1)).notify(any(Alert.class));
    }
}
