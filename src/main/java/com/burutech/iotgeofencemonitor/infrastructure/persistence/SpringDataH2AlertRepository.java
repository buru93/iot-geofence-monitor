package com.burutech.iotgeofencemonitor.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpringDataH2AlertRepository extends JpaRepository<H2AlertEntity, UUID> {
}
