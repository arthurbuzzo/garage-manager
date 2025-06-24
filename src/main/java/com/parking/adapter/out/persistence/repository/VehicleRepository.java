package com.parking.adapter.out.persistence.repository;

import com.parking.adapter.out.persistence.entity.VehicleEntity;
import com.parking.domain.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<VehicleEntity, Long> {
    
    Optional<VehicleEntity> findByLicensePlate(String licensePlate);
    
    @Query("SELECT v FROM VehicleEntity v WHERE v.licensePlate = :licensePlate AND v.status = :status")
    Optional<VehicleEntity> findActiveByLicensePlateAndStatus(@Param("licensePlate") String licensePlate, @Param("status") Vehicle.VehicleStatus status);
    
    List<VehicleEntity> findBySectorAndStatus(String sector, Vehicle.VehicleStatus status);
    
    List<VehicleEntity> findByStatus(Vehicle.VehicleStatus status);
}