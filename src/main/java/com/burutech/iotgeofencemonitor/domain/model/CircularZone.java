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
        double distance = calculateHaversineDistance(center, location);
        return distance <= radius;
    }

    // Optimized Haversine formula
    private double calculateHaversineDistance(Location loc1, Location loc2) {
        final int R = 6371000; // Earth radius in meters

        double lat1 = Math.toRadians(loc1.latitude());
        double lat2 = Math.toRadians(loc2.latitude());
        double lon1 = Math.toRadians(loc1.longitude());
        double lon2 = Math.toRadians(loc2.longitude());

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double a = Math.pow(Math.sin(dLat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                        * Math.pow(Math.sin(dLon / 2), 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }
}
