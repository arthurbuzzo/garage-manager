package com.parking.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {
    private String licensePlate;
    private LocalDateTime entryTime;
    private LocalDateTime parkingTime;
    private LocalDateTime exitTime;
    private Double latitude;
    private Double longitude;
    private String sector;
    private Integer spotId;
    private VehicleStatus status;
    
    public enum VehicleStatus {
        ENTERED,
        PARKED,
        EXITED
    }
}