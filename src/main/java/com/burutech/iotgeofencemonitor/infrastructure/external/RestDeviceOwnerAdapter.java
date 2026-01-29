package com.burutech.iotgeofencemonitor.infrastructure.external;

import com.burutech.iotgeofencemonitor.application.ports.output.DeviceOwnerRepositoryPort;
import com.burutech.iotgeofencemonitor.domain.model.DeviceOwner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RestDeviceOwnerAdapter implements DeviceOwnerRepositoryPort {

    @Override
    public Optional<DeviceOwner> findOwnerByDeviceId(String deviceId) {
        // TODO: IMPLEMENT THE SERVICE IN THE FUTURE
        if ("test-device".equals(deviceId)) {
            return Optional.of(new DeviceOwner("owner-1", "user@test.com", "Test User"));
        }
        return Optional.empty();
    }
}
