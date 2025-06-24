package com.parking.adapter.out.persistence.repository;

import com.parking.adapter.out.persistence.entity.ParkingSpotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingSpotRepository extends JpaRepository<ParkingSpotEntity, Integer> {
    
    Optional<ParkingSpotEntity> findByLatitudeAndLongitude(Double latitude, Double longitude);
    
    List<ParkingSpotEntity> findBySector(String sector);
    
    @Query("SELECT p FROM ParkingSpotEntity p WHERE p.sector = ?3 AND p.occupied = false " +
           "ORDER BY SQRT(POWER(p.latitude - ?1, 2) + POWER(p.longitude - ?2, 2)) LIMIT 1")
    Optional<ParkingSpotEntity> findNearestAvailable(Double lat, Double lng, String sector);
}
