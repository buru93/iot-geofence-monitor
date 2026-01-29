package com.burutech.iotgeofencemonitor.application.ports.input;

import com.burutech.iotgeofencemonitor.domain.model.DeviceSignal;

public interface MonitorDeviceUseCase {
    void processSignal(DeviceSignal signal);
}
