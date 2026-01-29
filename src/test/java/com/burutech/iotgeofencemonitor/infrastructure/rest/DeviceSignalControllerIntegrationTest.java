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

        String deviceId = "test-device";
        long initialAlertCount = alertRepository.count();

        SignalRequest request = new SignalRequest();
        request.setDeviceId(deviceId);
        request.setLatitude(40.416775); // Madrid
        request.setLongitude(-3.703790);
        request.setTimestamp(Instant.now());

        mockMvc.perform(post("/api/v1/signals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        long finalAlertCount = alertRepository.count();
        assertThat(finalAlertCount).isGreaterThan(initialAlertCount);
    }
}
