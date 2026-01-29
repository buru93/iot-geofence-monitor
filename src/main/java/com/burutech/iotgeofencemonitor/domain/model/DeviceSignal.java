package com.burutech.iotgeofencemonitor.domain.model;

import java.time.Instant;

public record DeviceSignal(String deviceId, Location location, Instant occurredAt) {
}
