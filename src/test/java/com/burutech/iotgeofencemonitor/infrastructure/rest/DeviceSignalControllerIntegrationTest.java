package com.burutech.iotgeofencemonitor.infrastructure.rest;

import com.burutech.iotgeofencemonitor.infrastructure.persistence.SpringDataH2AlertRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DeviceSignalControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SpringDataH2AlertRepository alertRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldProcessSignal_andCreateAlert_whenDeviceIsOutsideZone() throws Exception {
        // Arrange
        // We use "test-device" so the RestDeviceOwnerAdapter (dummy) finds an owner
        String deviceId = "test-device";

        // Location: Madrid Center (Inside Zone 1 from data.sql) should NOT trigger
        // alert.
        // Location: Somewhere else?
        // Let's look at data.sql:
        // Zone 1: 40.416775, -3.703790, Radius 1000m.
        // Zone 2: 41.3851, 2.1734, Radius 500.0 (Barcelona).

        // We want to trigger an alert.
        // If we are at Madrid coordinates, we are INSIDE Zone 1 -> No alert for Zone 1.
        // But we are definitely OUTSIDE Zone 2 (Barcelona).
        // The service logic iterates ALL zones.
        // For Zone 1: Inside -> No action.
        // For Zone 2: Outside (Alert!) -> handleOutsideZone -> Owner found -> Alert
        // saved.

        // So sending a signal at Madrid SHOULD trigger an alert for being outside
        // Barcelona zone.
        // Wait, normally geofencing alerts are for EXITING a specific zone you were
        // tracking.
        // But our current logic is: "Iterate ALL zones. If NOT inside, trigger alert."
        // That means if we have 2 disjoint zones (Madrid, Barcelona), and I am in
        // Madrid:
        // - I am inside Madrid (Good).
        // - I am OUTSIDE Barcelona (Bad? -> Alert).

        // The prompt requirement said: "Itera las zonas. Verifica
        // zone.isInside(location). Si NO está dentro... crea Alerta."
        // So yes, with current simple logic, being in Madrid will trigger an alert for
        // "Exiting Barcelona".
        // This is expected for this MVP logic iteration.

        long initialAlertCount = alertRepository.count();

        SignalRequest request = new SignalRequest();
        request.setDeviceId(deviceId);
        request.setLatitude(40.416775); // Madrid
        request.setLongitude(-3.703790);
        request.setTimestamp(Instant.now());

        // Act
        mockMvc.perform(post("/api/v1/signals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        // Assert
        // We expect at least one alert (because we are outside Zone 2)
        long finalAlertCount = alertRepository.count();
        assertThat(finalAlertCount).isGreaterThan(initialAlertCount);
    }
}
