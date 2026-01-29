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
        // TODO: IMPLEMENT THE SERVICE IN THE FUTURE
        log.info("[NOTIFICATION SENT] To: {} | Message: {}", alert.getOwner().email(), alert.getMessage());
    }
}
