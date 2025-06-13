package org.system.monitoring.domain.service;

import org.system.monitoring.application.dto.LocationDTO;

import java.util.Map;

public interface LocationService {
    void updateLocation(LocationDTO locationDTO);
    LocationDTO getLocation(String truckID);
    Map<String, LocationDTO> getAll();

}
