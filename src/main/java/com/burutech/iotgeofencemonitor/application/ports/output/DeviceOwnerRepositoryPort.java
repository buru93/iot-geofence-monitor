package com.burutech.iotgeofencemonitor.application.ports.output;

import com.burutech.iotgeofencemonitor.domain.model.DeviceOwner;
import java.util.Optional;

public interface DeviceOwnerRepositoryPort {
    Optional<DeviceOwner> findOwnerByDeviceId(String deviceId);
}
