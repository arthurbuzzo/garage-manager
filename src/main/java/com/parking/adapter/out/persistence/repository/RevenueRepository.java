package com.parking.adapter.out.persistence.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.parking.adapter.out.persistence.entity.RevenueEntity;

@Repository
public interface RevenueRepository extends JpaRepository<RevenueEntity, Long> {
    
    @Query("SELECT r FROM RevenueEntity r WHERE CAST(r.timestamp as DATE) = :date AND r.sector = :sector")
    List<RevenueEntity> findByDateAndSector(@Param("date") LocalDate date, @Param("sector") String sector);
    
    @Query("SELECT SUM(r.amount) FROM RevenueEntity r WHERE CAST(r.timestamp as DATE) = :date AND r.sector = :sector")
    Double sumByDateAndSector(@Param("date") LocalDate date, @Param("sector") String sector);
}