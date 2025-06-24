package com.parking.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Revenue {
    private Long id;
    private String licensePlate;
    private String sector;
    private Double amount;
    private LocalDateTime timestamp;
    private String currency;
}