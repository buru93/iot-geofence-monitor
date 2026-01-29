package com.burutech.iotgeofencemonitor.application.ports.output;

import com.burutech.iotgeofencemonitor.domain.model.Zone;

public interface SaveZonePort {
    void save(Zone zone);
}
