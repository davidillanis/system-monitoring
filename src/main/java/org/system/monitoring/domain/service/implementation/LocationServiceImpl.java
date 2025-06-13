package org.system.monitoring.domain.service.implementation;

import org.springframework.stereotype.Service;
import org.system.monitoring.application.dto.LocationDTO;
import org.system.monitoring.domain.service.LocationService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LocationServiceImpl implements LocationService {
    private final Map<String, LocationDTO> locationDTOMap = new ConcurrentHashMap<>();

    @Override
    public void updateLocation(LocationDTO locationDTO) {
        locationDTOMap.put(locationDTO.getTruckID(), locationDTO);
    }

    @Override
    public LocationDTO getLocation(String truckID) {
        return locationDTOMap.get(truckID);
    }

    @Override
    public Map<String, LocationDTO> getAll() {
        return locationDTOMap;
    }
}
