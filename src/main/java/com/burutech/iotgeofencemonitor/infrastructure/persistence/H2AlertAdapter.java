package com.burutech.iotgeofencemonitor.infrastructure.persistence;

import com.burutech.iotgeofencemonitor.application.ports.output.SaveAlertPort;
import com.burutech.iotgeofencemonitor.domain.model.Alert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class H2AlertAdapter implements SaveAlertPort {

    private final SpringDataH2AlertRepository repository;

    @Override
    public void save(Alert alert) {
        H2AlertEntity entity = new H2AlertEntity(
                alert.getId(),
                alert.getSignal().deviceId(),
                alert.getMessage(),
                alert.getSignal().occurredAt());
        repository.save(entity);
    }
}
