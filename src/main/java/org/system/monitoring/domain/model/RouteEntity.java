package org.system.monitoring.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString
public class RouteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer routeId;
    private LocalTime routeDuration;
    private LocalDateTime creationDate;
    @Column(length = 50)
    private String origin;
    @Column(length = 50)
    private String destination;
    private Boolean status;
}

