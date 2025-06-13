package org.system.monitoring.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString
public class CompactorTruck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 20, nullable = false)
    private String plate;

    @Column(length = 50)
    private String brand;

    @Column(length = 50)
    private String model;

    @Column(precision = 10, scale = 2)
    private BigDecimal compactorCapacity;

    private Boolean compactorStatus;

    private LocalDate creationDate;

}