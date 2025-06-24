package com.parking.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "revenues")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RevenueEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "license_plate", nullable = false)
    private String licensePlate;
    
    @Column(name = "sector", nullable = false)
    private String sector;
    
    @Column(name = "amount", nullable = false)
    private Double amount;
    
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
    
    @Column(name = "currency", nullable = false)
    private String currency;
}