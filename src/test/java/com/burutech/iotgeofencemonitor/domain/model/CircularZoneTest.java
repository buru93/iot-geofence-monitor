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
        Location center = new Location(0.0, 0.0);
        double radius = 100.0;
        CircularZone zone = new CircularZone("zone-edge", "Edge Test", center, radius);
        double targetDistance = 100.0;
        double earthRadius = 6371000.0;
        double dLatDegrees = (targetDistance / earthRadius) * (180.0 / Math.PI);

        Location edgeLocation = new Location(dLatDegrees, 0.0);

        assertThat(zone.isInside(edgeLocation)).isTrue();
    }

    @Test
    void shouldHandleZeroRadius() {
        Location center = new Location(10.0, 20.0);
        CircularZone zone = new CircularZone("zone-zero", "Zero Radius", center, 0.0);

        assertThat(zone.isInside(center)).isTrue();

        Location slightlyOff = new Location(10.00001, 20.0);
        assertThat(zone.isInside(slightlyOff)).isFalse();
    }
}
