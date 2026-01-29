package com.burutech.iotgeofencemonitor.domain.model;

public record Location(double latitude, double longitude) {

    /**
     * Calculates the distance in meters to another location using the Haversine
     * formula.
     *
     * @param other the target location
     * @return distance in meters
     * @throws IllegalArgumentException if other is null
     */
    public double distanceTo(Location other) {
        if (other == null) {
            throw new IllegalArgumentException("Target location cannot be null");
        }

        final int EARTH_RADIUS_METERS = 6371000;

        double thisLatRad = Math.toRadians(this.latitude);
        double otherLatRad = Math.toRadians(other.latitude);
        double thisLonRad = Math.toRadians(this.longitude);
        double otherLonRad = Math.toRadians(other.longitude);

        double deltaLat = otherLatRad - thisLatRad;
        double deltaLon = otherLonRad - thisLonRad;

        double sinHalfDeltaLat = Math.sin(deltaLat / 2);
        double sinHalfDeltaLon = Math.sin(deltaLon / 2);

        double squareOfHalfChordLength = sinHalfDeltaLat * sinHalfDeltaLat
                + Math.cos(thisLatRad) * Math.cos(otherLatRad)
                        * sinHalfDeltaLon * sinHalfDeltaLon;

        double centralAngleRadians = 2 * Math.atan2(Math.sqrt(squareOfHalfChordLength),
                Math.sqrt(1 - squareOfHalfChordLength));

        return EARTH_RADIUS_METERS * centralAngleRadians;
    }
}
