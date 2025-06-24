package com.parking.application.usecase;

import com.parking.domain.model.ParkingSpot;
import com.parking.domain.model.Sector;
import com.parking.domain.port.out.ParkingSpotPort;
import com.parking.domain.port.out.SectorPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class GarageConfigUseCase {
    
    private final SectorPort sectorPort;
    private final ParkingSpotPort parkingSpotPort;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    
    public void initializeGarage(List<SectorConfig> sectors, List<SpotConfig> spots) {
        log.info("Initializing garage with {} sectors and {} spots", sectors.size(), spots.size());
        
        parkingSpotPort.deleteAll();
        sectorPort.deleteAll();
        
        for (SectorConfig config : sectors) {
            Sector sector = Sector.builder()
                    .id(config.getSector())
                    .basePrice(config.getBasePrice())
                    .maxCapacity(config.getMaxCapacity())
                    .openHour(LocalTime.parse(config.getOpenHour(), TIME_FORMATTER))
                    .closeHour(LocalTime.parse(config.getCloseHour(), TIME_FORMATTER))
                    .durationLimitMinutes(config.getDurationLimitMinutes())
                    .currentOccupancy(0)
                    .build();
                    
            sectorPort.save(sector);
        }
        
        for (SpotConfig config : spots) {
            ParkingSpot spot = ParkingSpot.builder()
                    .id(config.getId())
                    .sector(config.getSector())
                    .latitude(config.getLat())
                    .longitude(config.getLng())
                    .occupied(false)
                    .build();
                    
            parkingSpotPort.save(spot);
        }
        
        log.info("Garage initialization completed");
    }
    
    public List<Sector> getAllSectors() {
        return sectorPort.findAll();
    }
    
    public List<ParkingSpot> getAllSpots() {
        return parkingSpotPort.findAll();
    }
    
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class SectorConfig {
        private String sector;
        private Double basePrice;
        private Integer maxCapacity;
        private String openHour;
        private String closeHour;
        private Integer durationLimitMinutes;
    }
    
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class SpotConfig {
        private Integer id;
        private String sector;
        private Double lat;
        private Double lng;
    }
}