package org.system.monitoring.domain.repository;

import org.system.monitoring.domain.model.CompactorTruck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompactorTruckRepository extends JpaRepository<CompactorTruck, Integer> {
    List<CompactorTruck> findByPlateContainingIgnoreCase(String plate);
    List<CompactorTruck> findByCompactorStatus(Boolean compactorStatus);
}
