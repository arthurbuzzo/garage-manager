package com.parking.adapter.out.persistence;

import com.parking.adapter.out.persistence.entity.VehicleEntity;
import com.parking.adapter.out.persistence.repository.VehicleRepository;
import com.parking.domain.model.Vehicle;
import com.parking.domain.port.out.VehiclePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class VehicleAdapter implements VehiclePort {
    
    private final VehicleRepository repository;
    
    @Override
    public Vehicle save(Vehicle vehicle) {
        VehicleEntity entity = toEntity(vehicle);
        VehicleEntity saved = repository.save(entity);
        return toDomain(saved);
    }
    
    @Override
    public Optional<Vehicle> findByLicensePlate(String licensePlate) {
        return repository.findByLicensePlate(licensePlate)
                .map(this::toDomain);
    }
    
    @Override
    public Optional<Vehicle> findActiveByLicensePlateAndStatus(String licensePlate, Vehicle.VehicleStatus status) {
        return repository.findActiveByLicensePlateAndStatus(licensePlate, status)
                .map(this::toDomain);
    }
    
    @Override
    public List<Vehicle> findBySectorAndStatus(String sector, Vehicle.VehicleStatus status) {
        return repository.findBySectorAndStatus(sector, status)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Vehicle> findByStatus(Vehicle.VehicleStatus status) {
        return repository.findByStatus(status)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
    
    private Vehicle toDomain(VehicleEntity entity) {
        return Vehicle.builder()
                .licensePlate(entity.getLicensePlate())
                .entryTime(entity.getEntryTime())
                .parkingTime(entity.getParkingTime())
                .exitTime(entity.getExitTime())
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .sector(entity.getSector())
                .spotId(entity.getSpotId())
                .status(entity.getStatus())
                .build();
    }
    
    private VehicleEntity toEntity(Vehicle vehicle) {
        return VehicleEntity.builder()
                .licensePlate(vehicle.getLicensePlate())
                .entryTime(vehicle.getEntryTime())
                .parkingTime(vehicle.getParkingTime())
                .exitTime(vehicle.getExitTime())
                .latitude(vehicle.getLatitude())
                .longitude(vehicle.getLongitude())
                .sector(vehicle.getSector())
                .spotId(vehicle.getSpotId())
                .status(vehicle.getStatus())
                .build();
    }
}