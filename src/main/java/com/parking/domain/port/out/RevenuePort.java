package com.parking.domain.port.out;

import com.parking.domain.model.Revenue;
import java.time.LocalDate;
import java.util.List;

public interface RevenuePort {
    Revenue save(Revenue revenue);
    List<Revenue> findByDateAndSector(LocalDate date, String sector);
    Double sumByDateAndSector(LocalDate date, String sector);
}