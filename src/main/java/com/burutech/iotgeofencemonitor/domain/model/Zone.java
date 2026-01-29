package com.burutech.iotgeofencemonitor.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class Zone {
    private final String id;
    private final String name;

    public abstract boolean isInside(Location location);
}
