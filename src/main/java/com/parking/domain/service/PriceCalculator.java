package com.parking.domain.service;

import com.parking.domain.model.Sector;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class PriceCalculator {
    
    public double calculatePrice(Sector sector, LocalDateTime entryTime, LocalDateTime currentTime) {
        if (entryTime == null || currentTime == null) {
            return 0.0;
        }
        
        Duration duration = Duration.between(entryTime, currentTime);
        long hours = Math.max(1, (long) Math.ceil(duration.toMinutes() / 60.0));
        
        double basePrice = sector.getBasePrice();
        double adjustedPrice = applyDynamicPricing(basePrice, sector.getOccupancyRate());
        
        return adjustedPrice * hours;
    }
    
    private double applyDynamicPricing(double basePrice, double occupancyRate) {
        if (occupancyRate < 25) {
            return basePrice * 0.9; // 10% desconto
        } else if (occupancyRate <= 50) {
            return basePrice; // sem alteracao
        } else if (occupancyRate <= 75) {
            return basePrice * 1.1; // 10% incremento
        } else {
            return basePrice * 1.25; // 25% incremento
        }
    }
}