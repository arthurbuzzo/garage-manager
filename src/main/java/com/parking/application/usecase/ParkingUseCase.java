package com.parking.application.usecase;

import com.parking.domain.model.*;
import com.parking.domain.model.Vehicle.VehicleStatus;
import com.parking.domain.port.out.*;
import com.parking.domain.service.PriceCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ParkingUseCase {
    
    private final VehiclePort vehiclePort;
    private final SectorPort sectorPort;
    private final ParkingSpotPort parkingSpotPort;
    private final RevenuePort revenuePort;
    private final PriceCalculator priceCalculator;
    
    public void handleVehicleEntry(String licensePlate, LocalDateTime entryTime) {
        log.info("Vehicle entry: {} at {}", licensePlate, entryTime);
        
        Vehicle vehicle = Vehicle.builder()
                .licensePlate(licensePlate)
                .entryTime(entryTime)
                .status(Vehicle.VehicleStatus.ENTERED)
                .build();
                
        vehiclePort.save(vehicle);
    }
    
    public Optional<Vehicle> getVehicleStatusParked(String licensePlate) {
        return vehiclePort.findActiveByLicensePlateAndStatus(licensePlate, VehicleStatus.PARKED);
    }
    
    public Optional<ParkingSpot> getSpotStatus(Double lat, Double lng) {
        return parkingSpotPort.findByCoordinates(lat, lng);
    }
    
    public Double getRevenue(LocalDate date, String sector) {
        Double revenue = revenuePort.sumByDateAndSector(date, sector);
        return revenue != null ? revenue : 0.0;
    }
    
    public double calculateCurrentPrice(String licensePlate) {
        Optional<Vehicle> vehicleOpt = vehiclePort.findActiveByLicensePlateAndStatus(licensePlate, VehicleStatus.ENTERED);
        if (vehicleOpt.isEmpty()) {
            return 0.0;
        }
        
        Vehicle vehicle = vehicleOpt.get();
        if (vehicle.getSector() == null || vehicle.getEntryTime() == null) {
            return 0.0;
        }
        
        Optional<Sector> sectorOpt = sectorPort.findById(vehicle.getSector());
        if (sectorOpt.isEmpty()) {
            return 0.0;
        }
        
        return priceCalculator.calculatePrice(sectorOpt.get(), vehicle.getEntryTime(), LocalDateTime.now());
    }
    
    public void handleVehicleParking(String licensePlate, Double lat, Double lng) {
        log.info("Vehicle parking: {} at ({}, {})", licensePlate, lat, lng);
        
        Optional<Vehicle> vehicleOpt = vehiclePort.findActiveByLicensePlateAndStatus(licensePlate, VehicleStatus.ENTERED);
        if (vehicleOpt.isEmpty()) {
            throw new IllegalStateException("Vehicle not found or already exited: " + licensePlate);
        }
        
        Optional<ParkingSpot> spotOpt = parkingSpotPort.findByCoordinates(lat, lng);
        if (spotOpt.isEmpty()) {
            throw new IllegalStateException("Parking spot not found at coordinates");
        }
        
        ParkingSpot spot = spotOpt.get();
        if (spot.isOccupied()) {
            throw new IllegalStateException("Parking spot is already occupied");
        }
        
        // Update sector occupancy
        Optional<Sector> sectorOpt = sectorPort.findById(spot.getSector());
        if (sectorOpt.isPresent()) {
            Sector sector = sectorOpt.get();
            if (sector.isFull()) {
                throw new IllegalStateException("Sector is full");
            }
            sector.incrementOccupancy();
            sectorPort.save(sector);
        }
        
        // Update vehicle
        Vehicle vehicle = vehicleOpt.get();
        vehicle.setParkingTime(LocalDateTime.now());
        vehicle.setLatitude(lat);
        vehicle.setLongitude(lng);
        vehicle.setSector(spot.getSector());
        vehicle.setSpotId(spot.getId());
        vehicle.setStatus(Vehicle.VehicleStatus.PARKED);
        vehiclePort.save(vehicle);
        
        // Update spot
        spot.setOccupied(true);
        spot.setOccupiedBy(licensePlate);
        parkingSpotPort.save(spot);
    }
    
    public void handleVehicleExit(String licensePlate, LocalDateTime exitTime) {
        log.info("Vehicle exit: {} at {}", licensePlate, exitTime);
        
        Optional<Vehicle> vehicleOpt = vehiclePort.findActiveByLicensePlateAndStatus(licensePlate, VehicleStatus.ENTERED);
        if (vehicleOpt.isEmpty()) {
            throw new IllegalStateException("Vehicle not found or already exited: " + licensePlate);
        }
        
        Vehicle vehicle = vehicleOpt.get();
        vehicle.setExitTime(exitTime);
        vehicle.setStatus(Vehicle.VehicleStatus.EXITED);
        
        // Calculate and save revenue
        if (vehicle.getSector() != null) {
            Optional<Sector> sectorOpt = sectorPort.findById(vehicle.getSector());
            if (sectorOpt.isPresent()) {
                Sector sector = sectorOpt.get();
                double price = priceCalculator.calculatePrice(sector, vehicle.getEntryTime(), exitTime);
                
                Revenue revenue = Revenue.builder()
                        .licensePlate(licensePlate)
                        .sector(vehicle.getSector())
                        .amount(price)
                        .timestamp(exitTime)
                        .currency("BRL")
                        .build();
                        
                revenuePort.save(revenue);
                
                // Update sector occupancy
                sector.decrementOccupancy();
                sectorPort.save(sector);
            }
        }
        
        // Free the parking spot
        if (vehicle.getSpotId() != null) {
            Optional<ParkingSpot> spotOpt = parkingSpotPort.findById(vehicle.getSpotId());
            if (spotOpt.isPresent()) {
                ParkingSpot spot = spotOpt.get();
                spot.setOccupied(false);
                spot.setOccupiedBy(null);
                parkingSpotPort.save(spot);
            }
        }
    }
        
}