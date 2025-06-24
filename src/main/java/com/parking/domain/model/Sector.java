package com.parking.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sector {
    private String id;
    private Double basePrice;
    private Integer maxCapacity;
    private LocalTime openHour;
    private LocalTime closeHour;
    private Integer durationLimitMinutes;
    private Integer currentOccupancy;
    
    public double getOccupancyRate() {
        if (maxCapacity == 0) return 0;
        return (double) currentOccupancy / maxCapacity * 100;
    }
    
    public boolean isFull() {
        return currentOccupancy >= maxCapacity;
    }
    
    public void incrementOccupancy() {
        if (!isFull()) {
            currentOccupancy++;
        }
    }
    
    public void decrementOccupancy() {
        if (currentOccupancy > 0) {
            currentOccupancy--;
        }
    }
}