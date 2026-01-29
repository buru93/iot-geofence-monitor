package com.burutech.iotgeofencemonitor.infrastructure.external;

import com.burutech.iotgeofencemonitor.application.ports.output.NotificationPort;
import com.burutech.iotgeofencemonitor.domain.model.Alert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HttpNotificationAdapter implements NotificationPort {

    @Override
    public void notify(Alert alert) {
        // TODO: Implement real HTTP call to Notification Service
        log.info("[NOTIFICATION SENT] To: {} | Message: {}", alert.getOwner().email(), alert.getMessage());
    }
}
