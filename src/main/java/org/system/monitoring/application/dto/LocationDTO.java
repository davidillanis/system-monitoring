package org.system.monitoring.application.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class LocationDTO {
    private String truckID;
    private double latitude;
    private double longitude;
    private Instant timestamp=Instant.now();
    private String status;
}
