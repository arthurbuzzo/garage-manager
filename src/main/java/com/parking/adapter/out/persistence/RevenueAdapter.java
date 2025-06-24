package com.parking.adapter.out.persistence;

import com.parking.adapter.out.persistence.entity.RevenueEntity;
import com.parking.adapter.out.persistence.repository.RevenueRepository;
import com.parking.domain.model.Revenue;
import com.parking.domain.port.out.RevenuePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RevenueAdapter implements RevenuePort {
    
    private final RevenueRepository repository;
    
    @Override
    public Revenue save(Revenue revenue) {
        RevenueEntity entity = toEntity(revenue);
        RevenueEntity saved = repository.save(entity);
        return toDomain(saved);
    }
    
    @Override
    public List<Revenue> findByDateAndSector(LocalDate date, String sector) {
        return repository.findByDateAndSector(date, sector).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public Double sumByDateAndSector(LocalDate date, String sector) {
        return repository.sumByDateAndSector(date, sector);
    }
    
    private Revenue toDomain(RevenueEntity entity) {
        return Revenue.builder()
                .id(entity.getId())
                .licensePlate(entity.getLicensePlate())
                .sector(entity.getSector())
                .amount(entity.getAmount())
                .timestamp(entity.getTimestamp())
                .currency(entity.getCurrency())
                .build();
    }
    
    private RevenueEntity toEntity(Revenue revenue) {
        return RevenueEntity.builder()
                .id(revenue.getId())
                .licensePlate(revenue.getLicensePlate())
                .sector(revenue.getSector())
                .amount(revenue.getAmount())
                .timestamp(revenue.getTimestamp())
                .currency(revenue.getCurrency())
                .build();
    }
}