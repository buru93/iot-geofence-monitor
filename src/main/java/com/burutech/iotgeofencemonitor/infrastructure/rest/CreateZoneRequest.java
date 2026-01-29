package com.burutech.iotgeofencemonitor.infrastructure.rest;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateZoneRequest {
    @NotBlank(message = "Zone name is required")
    private String name;

    @NotNull(message = "Latitude is required")
    private Double latitude;

    @NotNull(message = "Longitude is required")
    private Double longitude;

    @NotNull(message = "Radius is required")
    @Min(value = 1, message = "Radius must be at least 1 meter")
    private Double radius;
}
