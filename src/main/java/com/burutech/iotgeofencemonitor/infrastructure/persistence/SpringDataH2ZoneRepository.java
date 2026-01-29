package com.burutech.iotgeofencemonitor.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataH2ZoneRepository extends JpaRepository<H2ZoneEntity, String> {
}
