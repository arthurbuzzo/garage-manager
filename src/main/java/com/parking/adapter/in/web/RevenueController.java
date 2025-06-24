package com.parking.adapter.in.web;

import com.parking.adapter.in.web.api.RevenueApi;
import com.parking.adapter.in.web.model.RevenueResponse;
import com.parking.application.usecase.ParkingUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RevenueController implements RevenueApi {
    
    private final ParkingUseCase parkingUseCase;
    
    @Override
    public ResponseEntity<RevenueResponse> _getRevenue(LocalDate date, String sector) {
    	log.info("Getting revenue for date {} and sector {}", date, sector);
        
        Double totalRevenue = parkingUseCase.getRevenue(date, sector);
        
        RevenueResponse response = new RevenueResponse();
        response.setAmount(totalRevenue);
        response.setCurrency("BRL");
        response.setTimestamp(LocalDateTime.now());
        
        return ResponseEntity.ok(response);
    }
    
}