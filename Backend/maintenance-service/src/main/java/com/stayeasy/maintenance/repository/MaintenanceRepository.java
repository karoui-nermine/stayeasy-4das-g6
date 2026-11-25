package com.stayeasy.maintenance.repository;

import com.stayeasy.maintenance.entity.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {
}
