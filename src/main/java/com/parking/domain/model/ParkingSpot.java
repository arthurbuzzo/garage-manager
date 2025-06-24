package com.parking.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSpot {
    private Integer id;
    private String sector;
    private Double latitude;
    private Double longitude;
    private boolean occupied;
    private String occupiedBy; 
}