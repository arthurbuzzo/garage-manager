package com.parking.domain.port.out;

import com.parking.domain.model.Vehicle;
import java.util.List;
import java.util.Optional;

public interface VehiclePort {
    Vehicle save(Vehicle vehicle);
    Optional<Vehicle> findByLicensePlate(String licensePlate);
    Optional<Vehicle> findActiveByLicensePlateAndStatus(String licensePlate, Vehicle.VehicleStatus status);
    List<Vehicle> findBySectorAndStatus(String sector, Vehicle.VehicleStatus status);
    List<Vehicle> findByStatus(Vehicle.VehicleStatus status);
}