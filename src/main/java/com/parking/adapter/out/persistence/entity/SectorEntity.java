package com.parking.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Table(name = "sectors")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SectorEntity {
    
    @Id
    private String id;
    
    @Column(name = "base_price", nullable = false)
    private Double basePrice;
    
    @Column(name = "max_capacity", nullable = false)
    private Integer maxCapacity;
    
    @Column(name = "open_hour", nullable = false)
    private LocalTime openHour;
    
    @Column(name = "close_hour", nullable = false)
    private LocalTime closeHour;
    
    @Column(name = "duration_limit_minutes", nullable = false)
    private Integer durationLimitMinutes;
    
    @Column(name = "current_occupancy", nullable = false)
    private Integer currentOccupancy;
}