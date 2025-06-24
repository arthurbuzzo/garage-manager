package com.parking.domain.port.out;

import com.parking.domain.model.ParkingSpot;
import java.util.List;
import java.util.Optional;

public interface ParkingSpotPort {
    ParkingSpot save(ParkingSpot spot);
    Optional<ParkingSpot> findById(Integer id);
    Optional<ParkingSpot> findByCoordinates(Double lat, Double lng);
    Optional<ParkingSpot> findNearestAvailable(Double lat, Double lng, String sector);
    List<ParkingSpot> findBySector(String sector);
    List<ParkingSpot> findAll();
    void deleteAll();
}