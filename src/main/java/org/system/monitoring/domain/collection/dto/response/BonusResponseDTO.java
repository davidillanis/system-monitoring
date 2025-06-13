package org.system.monitoring.domain.collection.dto.response;

import org.system.monitoring.domain.collection.BonusCollection;
import org.system.monitoring.infrastructure.firebase.GenericResponse;

public class BonusResponseDTO implements GenericResponse<BonusCollection> {
    private String id;
    private String nameBonus;
    private String description;
    private int requiredTickets;
    private double status;
}
