package com.parking.adapter.out.persistence.repository;

import com.parking.adapter.out.persistence.entity.SectorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectorRepository extends JpaRepository<SectorEntity, String> {
}