package com.parking.adapter.in.web;

import com.parking.adapter.in.web.api.GarageApi;
import com.parking.adapter.in.web.model.GarageConfig;
import com.parking.adapter.in.web.model.Sector;
import com.parking.adapter.in.web.model.Spot;
import com.parking.application.usecase.GarageConfigUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GarageController implements GarageApi {
    
    private final GarageConfigUseCase garageConfigUseCase;
    
    @Override
    public ResponseEntity<GarageConfig> _getGarageConfig() {
        log.info("Getting garage configuration");
        
        List<GarageConfigUseCase.SectorConfig> sectorConfigs = List.of(
            GarageConfigUseCase.SectorConfig.builder()
                .sector("A")
                .basePrice(10.0)
                .maxCapacity(100)
                .openHour("08:00")
                .closeHour("22:00")
                .durationLimitMinutes(240)
                .build(),
            GarageConfigUseCase.SectorConfig.builder()
                .sector("B")
                .basePrice(4.0)
                .maxCapacity(72)
                .openHour("05:00")
                .closeHour("18:00")
                .durationLimitMinutes(120)
                .build()
        );
        
        List<GarageConfigUseCase.SpotConfig> spotConfigs = List.of(
            GarageConfigUseCase.SpotConfig.builder()
                .id(1)
                .sector("A")
                .lat(-23.561684)
                .lng(-46.655981)
                .build(),
            GarageConfigUseCase.SpotConfig.builder()
                .id(2)
                .sector("B")
                .lat(-23.561674)
                .lng(-46.655971)
                .build()
        );
        
        // Inicia a garagem
        garageConfigUseCase.initializeGarage(sectorConfigs, spotConfigs);
        
        // Create response
        GarageConfig config = new GarageConfig();
        
        List<Sector> sectors = sectorConfigs.stream()
            .map(sc -> {
                Sector sector = new Sector();
                sector.setSector(sc.getSector());
                sector.setBasePrice(sc.getBasePrice());
                sector.setMaxCapacity(sc.getMaxCapacity());
                sector.setOpenHour(sc.getOpenHour());
                sector.setCloseHour(sc.getCloseHour());
                sector.setDurationLimitMinutes(sc.getDurationLimitMinutes());
                return sector;
            })
            .collect(Collectors.toList());
        
        List<Spot> spots = spotConfigs.stream()
            .map(sp -> {
                Spot spot = new Spot();
                spot.setId(sp.getId());
                spot.setSector(sp.getSector());
                spot.setLat(sp.getLat());
                spot.setLng(sp.getLng());
                return spot;
            })
            .collect(Collectors.toList());
        
        config.setGarage(sectors);
        config.setSpots(spots);
        
        return ResponseEntity.ok(config);
    }
}