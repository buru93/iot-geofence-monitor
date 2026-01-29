package com.burutech.iotgeofencemonitor.application.ports.output;

import com.burutech.iotgeofencemonitor.domain.model.Zone;
import java.util.List;

public interface LoadZonesPort {
    List<Zone> getAllActiveZones();
}
