package com.parking.adapter.out.persistence;

import com.parking.adapter.out.persistence.entity.ParkingSpotEntity;
import com.parking.adapter.out.persistence.repository.ParkingSpotRepository;
import com.parking.domain.model.ParkingSpot;
import com.parking.domain.port.out.ParkingSpotPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ParkingSpotAdapter implements ParkingSpotPort {
    
    private final ParkingSpotRepository repository;
    
    @Override
    public ParkingSpot save(ParkingSpot spot) {
        ParkingSpotEntity entity = toEntity(spot);
        ParkingSpotEntity saved = repository.save(entity);
        return toDomain(saved);
    }
    
    @Override
    public Optional<ParkingSpot> findById(Integer id) {
        return repository.findById(id).map(this::toDomain);
    }
    
    @Override
    public Optional<ParkingSpot> findByCoordinates(Double lat, Double lng) {
        return repository.findByLatitudeAndLongitude(lat, lng).map(this::toDomain);
    }
    
    @Override
    public Optional<ParkingSpot> findNearestAvailable(Double lat, Double lng, String sector) {
        return repository.findNearestAvailable(lat, lng, sector).map(this::toDomain);
    }
    
    @Override
    public List<ParkingSpot> findBySector(String sector) {
        return repository.findBySector(sector).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ParkingSpot> findAll() {
        return repository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
    
    private ParkingSpot toDomain(ParkingSpotEntity entity) {
        return ParkingSpot.builder()
                .id(entity.getId())
                .sector(entity.getSector())
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .occupied(entity.isOccupied())
                .occupiedBy(entity.getOccupiedBy())
                .build();
    }
    
    private ParkingSpotEntity toEntity(ParkingSpot spot) {
        return ParkingSpotEntity.builder()
                .id(spot.getId())
                .sector(spot.getSector())
                .latitude(spot.getLatitude())
                .longitude(spot.getLongitude())
                .occupied(spot.isOccupied())
                .occupiedBy(spot.getOccupiedBy())
                .build();
    }
}
