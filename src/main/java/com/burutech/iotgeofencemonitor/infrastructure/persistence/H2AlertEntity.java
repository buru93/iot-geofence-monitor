package com.burutech.iotgeofencemonitor.infrastructure.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "alerts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class H2AlertEntity {
    @Id
    private UUID id;
    private String deviceId;
    private String message;
    private Instant timestamp;
}
