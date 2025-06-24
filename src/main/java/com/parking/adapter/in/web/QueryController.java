package com.parking.adapter.in.web;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.parking.adapter.in.web.api.QueryApi;
import com.parking.adapter.in.web.model.PlateStatusRequest;
import com.parking.adapter.in.web.model.PlateStatusResponse;
import com.parking.adapter.in.web.model.SpotStatusRequest;
import com.parking.adapter.in.web.model.SpotStatusResponse;
import com.parking.application.usecase.ParkingUseCase;
import com.parking.domain.model.ParkingSpot;
import com.parking.domain.model.Vehicle;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class QueryController implements QueryApi {
    
    private final ParkingUseCase parkingUseCase;
    
    @Override
    public ResponseEntity<PlateStatusResponse> _getPlateStatus(PlateStatusRequest plateStatusRequest) {
        log.info("Getting status for plate: {}", plateStatusRequest.getLicensePlate());
        
        Optional<Vehicle> vehicleOpt = parkingUseCase.getVehicleStatusParked(plateStatusRequest.getLicensePlate());
        
        if (vehicleOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Vehicle vehicle = vehicleOpt.get();
        double currentPrice = parkingUseCase.calculateCurrentPrice(vehicle.getLicensePlate());
        
        PlateStatusResponse response = new PlateStatusResponse();
        response.setLicensePlate(vehicle.getLicensePlate());
        response.setPriceUntilNow(currentPrice);
        response.setEntryTime(vehicle.getEntryTime());
        response.setTimeParked(vehicle.getParkingTime());
        response.setLat(vehicle.getLatitude());
        response.setLng(vehicle.getLongitude());
        
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<SpotStatusResponse> _getSpotStatus(SpotStatusRequest spotStatusRequest) {
        log.info("Getting status for spot at ({}, {})", 
            spotStatusRequest.getLat(), spotStatusRequest.getLng());
        
        Optional<ParkingSpot> spotOpt = parkingUseCase.getSpotStatus(
            spotStatusRequest.getLat(), 
            spotStatusRequest.getLng()
        );
        
        if (spotOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        ParkingSpot spot = spotOpt.get();
        SpotStatusResponse response = new SpotStatusResponse();
        response.setOcupied(spot.isOccupied());
        response.setLicensePlate(spot.getOccupiedBy() != null ? spot.getOccupiedBy() : "");
        
        if (spot.isOccupied() && spot.getOccupiedBy() != null) {
            Optional<Vehicle> vehicleOpt = parkingUseCase.getVehicleStatusParked(spot.getOccupiedBy());
            if (vehicleOpt.isPresent()) {
                Vehicle vehicle = vehicleOpt.get();
                double currentPrice = parkingUseCase.calculateCurrentPrice(vehicle.getLicensePlate());
                response.setPriceUntilNow(currentPrice);
                response.setEntryTime(vehicle.getEntryTime());
                response.setTimeParked(vehicle.getParkingTime());
            }
        } else {
            response.setPriceUntilNow(0.0);
        }
        
        return ResponseEntity.ok(response);
    }
}