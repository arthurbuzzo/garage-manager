package com.parking.adapter.out.persistence;

import com.parking.adapter.out.persistence.entity.SectorEntity;
import com.parking.adapter.out.persistence.repository.SectorRepository;
import com.parking.domain.model.Sector;
import com.parking.domain.port.out.SectorPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SectorAdapter implements SectorPort {
    
    private final SectorRepository repository;
    
    @Override
    public Sector save(Sector sector) {
        SectorEntity entity = toEntity(sector);
        SectorEntity saved = repository.save(entity);
        return toDomain(saved);
    }
    
    @Override
    public Optional<Sector> findById(String id) {
        return repository.findById(id).map(this::toDomain);
    }
    
    @Override
    public List<Sector> findAll() {
        return repository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
    
    private Sector toDomain(SectorEntity entity) {
        return Sector.builder()
                .id(entity.getId())
                .basePrice(entity.getBasePrice())
                .maxCapacity(entity.getMaxCapacity())
                .openHour(entity.getOpenHour())
                .closeHour(entity.getCloseHour())
                .durationLimitMinutes(entity.getDurationLimitMinutes())
                .currentOccupancy(entity.getCurrentOccupancy())
                .build();
    }
    
    private SectorEntity toEntity(Sector sector) {
        return SectorEntity.builder()
                .id(sector.getId())
                .basePrice(sector.getBasePrice())
                .maxCapacity(sector.getMaxCapacity())
                .openHour(sector.getOpenHour())
                .closeHour(sector.getCloseHour())
                .durationLimitMinutes(sector.getDurationLimitMinutes())
                .currentOccupancy(sector.getCurrentOccupancy())
                .build();
    }
}