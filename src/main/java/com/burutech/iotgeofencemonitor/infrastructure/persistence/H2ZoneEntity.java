package com.burutech.iotgeofencemonitor.infrastructure.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "zones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class H2ZoneEntity {
    @Id
    private String id;
    private double latitude;
    private double longitude;
    private double radius;
    private String name;
}
