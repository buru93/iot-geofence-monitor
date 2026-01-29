package com.burutech.iotgeofencemonitor.domain.model;

import lombok.Builder;
import lombok.Getter;
import java.util.UUID;

@Getter
@Builder
public class Alert {
    private final UUID id;
    private final DeviceSignal signal;
    private final String zoneId;
    private final String message;
    private final DeviceOwner owner;
}