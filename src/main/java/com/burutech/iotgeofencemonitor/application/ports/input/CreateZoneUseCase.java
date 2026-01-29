package com.burutech.iotgeofencemonitor.application.ports.input;

public interface CreateZoneUseCase {
    void createCircularZone(String name, double lat, double lon, double radius);
}
