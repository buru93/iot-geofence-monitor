package com.burutech.iotgeofencemonitor.infrastructure.rest;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
public class SignalRequest {
    @NotNull
    private String deviceId;
    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;
    @NotNull
    private Instant timestamp;
}
