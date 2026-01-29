package com.burutech.iotgeofencemonitor.application.ports.output;

import com.burutech.iotgeofencemonitor.domain.model.Alert;

public interface NotificationPort {
    void notify(Alert alert);
}
