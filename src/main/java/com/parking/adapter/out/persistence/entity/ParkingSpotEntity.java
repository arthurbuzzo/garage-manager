package com.parking.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "parking_spots")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSpotEntity {
    
    @Id
    private Integer id;
    
    @Column(name = "sector", nullable = false)
    private String sector;
    
    @Column(name = "latitude", nullable = false)
    private Double latitude;
    
    @Column(name = "longitude", nullable = false)
    private Double longitude;
    
    @Column(name = "occupied", nullable = false)
    private boolean occupied;
    
    @Column(name = "occupied_by")
    private String occupiedBy;
}