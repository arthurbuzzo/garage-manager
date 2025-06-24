package com.parking.domain.port.out;

import com.parking.domain.model.Sector;
import java.util.List;
import java.util.Optional;

public interface SectorPort {
    Sector save(Sector sector);
    Optional<Sector> findById(String id);
    List<Sector> findAll();
    void deleteAll();
}