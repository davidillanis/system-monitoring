package org.system.monitoring.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.system.monitoring.application.dto.LocationDTO;
import org.system.monitoring.domain.service.LocationService;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

@Controller
@RestController
@RequiredArgsConstructor
public class LocationWebSocketController {
    private final LocationService locationService;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Recibe actualizaciones de ubicación desde los operarios
     */
    @MessageMapping("/location/update")
    public void updateLocation(@Payload LocationDTO location) {
        try {
            locationService.updateLocation(location);
            messagingTemplate.convertAndSend("/topic/location/" + location.getTruckID(), location);
            messagingTemplate.convertAndSend("/topic/locations/all", locationService.getAll());
            System.out.println("Ubicación actualizada para camión: " + location.getTruckID() +
                    " - Lat: " + location.getLatitude() +
                    " - Lng: " + location.getLongitude() +
                    " - Estado: " + location.getStatus());

        } catch (Exception e) {
            System.err.println("Error actualizando ubicación: " + e.getMessage());
            messagingTemplate.convertAndSend("/topic/errors",
                    "Error actualizando ubicación del camión: " + location.getTruckID());
        }
    }

    /**
     * Solicita ubicación específica de un camión
     */
    @MessageMapping("/location/request")
    public void requestLocation(@Payload String truckID) {
        LocationDTO location = locationService.getLocation(truckID);
        messagingTemplate.convertAndSend("/topic/location/" + truckID, Objects.requireNonNullElseGet(location, () -> createNotFoundResponse(truckID)));
    }

    /**
     * Solicita todas las ubicaciones (para funcionarios)
     */
    @MessageMapping("/locations/requestAll")
    public void requestAllLocations() {
        Map<String, LocationDTO> allLocations = locationService.getAll();
        messagingTemplate.convertAndSend("/topic/locations/all", allLocations);
    }

    /**
     * Managements alert and special notifications
     */
    @MessageMapping("/alerts")
    public void handleAlert(@Payload Map<String, Object> alert) {
        String alertType = (String) alert.get("type");
        String truckID = (String) alert.get("truckID");
        String message = (String) alert.get("message");
        messagingTemplate.convertAndSend("/topic/alerts", alert);

        System.out.println("Alerta recibida - Tipo: " + alertType +
                " - Camión: " + truckID +
                " - Mensaje: " + message);
    }

    // ============ REST ENDPOINTS FOR HTTP CUSTOMER ============
    @GetMapping("/api/location/{truckID}")
    public LocationDTO getLocationRest(@PathVariable String truckID) {
        LocationDTO location = locationService.getLocation(truckID);
        return location != null ? location : createNotFoundResponse(truckID);
    }

    @GetMapping("/api/locations")
    public Map<String, LocationDTO> getAllLocationsRest() {
        return locationService.getAll();
    }

    // ============ AUX METHODS ============
    private LocationDTO createNotFoundResponse(String truckID) {
        LocationDTO notFound = new LocationDTO();
        notFound.setTruckID(truckID);
        notFound.setLatitude(0.0);
        notFound.setLongitude(0.0);
        notFound.setStatus("NOT_FOUND");
        notFound.setTimestamp(Instant.now());
        return notFound;
    }
}