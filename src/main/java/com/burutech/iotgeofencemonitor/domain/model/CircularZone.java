package com.burutech.iotgeofencemonitor.domain.model;

import lombok.Getter;

@Getter
public class CircularZone extends Zone {
    private final Location center;
    private final double radius; // meters

    public CircularZone(String id, String name, Location center, double radius) {
        super(id, name);
        this.center = center;
        this.radius = radius;
    }

    @Override
    public boolean isInside(Location location) {
        if (location == null) {
            return false;
        }
        return center.distanceTo(location) <= radius;
    }
}
