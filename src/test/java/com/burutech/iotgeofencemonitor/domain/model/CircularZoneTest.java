package com.burutech.iotgeofencemonitor.domain.model;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class CircularZoneTest {

    @Test
    void shouldReturnTrue_whenLocationIsInside() {
        // Center: Madrid (Puerta del Sol)
        Location center = new Location(40.416775, -3.703790);
        // Radius: 100 meters
        CircularZone zone = new CircularZone("zone-1", "Madrid Center", center, 100.0);

        // Location: close to center (approx 10m away)
        Location insideLocation = new Location(40.416865, -3.703790);

        assertThat(zone.isInside(insideLocation)).isTrue();
    }

    @Test
    void shouldReturnFalse_whenLocationIsOutside() {
        // Center: Madrid
        Location center = new Location(40.416775, -3.703790);
        // Radius: 100 meters
        CircularZone zone = new CircularZone("zone-1", "Madrid Center", center, 100.0);

        // Location: Atocha station (clearly outside, approx 1.5km away)
        Location outsideLocation = new Location(40.4065, -3.6896);

        assertThat(zone.isInside(outsideLocation)).isFalse();
    }

    @Test
    void shouldReturnTrue_whenLocationIsOnTheEdge() {
        // Center: (0,0) for simplicity in calculation logic verification if needed,
        // but let's use real coords to test the haversine specifically.
        // Let's manually calculate a point roughly on the edge or use a simpler case.
        // Easier: Use 0,0 and a point at 90 degrees latitude distance? No, that's too
        // far.
        // Let's just trust the haversine formula for "very close" to edge.
        // Better: Use a small radius and a known distance shift.
        // 1 degree lat is approx 111,111 meters.
        // 0.000009 degrees is approx 1 meter.

        Location center = new Location(0.0, 0.0);
        double radius = 100.0;
        CircularZone zone = new CircularZone("zone-edge", "Edge Test", center, radius);

        // We need a point exactly 100m away.
        // This is tricky with floating point and Haversine.
        // Let's test "almost" on edge to avoid strict equality issues with doubles,
        // OR test that it returns true for a calculated point that SHOULD be inside.
        // The prompt asks for "exactly on the radius".
        // Let's pick a point North.
        // Distance = R * c. We want Distance = 100.
        // c = 100 / R_EARTH = 100 / 6371000.
        // c = 2 * atan2(sqrt(a), sqrt(1-a)).
        // For pure North, dLon = 0, so a = sin^2(dLat/2).
        // sqrt(a) = sin(dLat/2).
        // c = 2 * dLat/2 = dLat.
        // So dLat (radians) = 100 / 6371000.
        // lat2 = lat1 + (100 / 6371000) * (180 / PI).

        double targetDistance = 100.0;
        double earthRadius = 6371000.0;
        double dLatDegrees = (targetDistance / earthRadius) * (180.0 / Math.PI);

        Location edgeLocation = new Location(dLatDegrees, 0.0);

        // Due to floating point math, it might be slightly off, so we might assert it
        // is mostly true
        // or ensure our Haversine implementation handles standard epsilon.
        // For the purpose of this test, we accept if it matches or is very close.
        // ideally isInside uses <= so exact match should be True.

        assertThat(zone.isInside(edgeLocation)).isTrue();
    }

    @Test
    void shouldHandleZeroRadius() {
        Location center = new Location(10.0, 20.0);
        CircularZone zone = new CircularZone("zone-zero", "Zero Radius", center, 0.0);

        // Exact center should be inside (distance 0 <= 0)
        assertThat(zone.isInside(center)).isTrue();

        // Anything else should be outside
        Location slightlyOff = new Location(10.00001, 20.0);
        assertThat(zone.isInside(slightlyOff)).isFalse();
    }
}
