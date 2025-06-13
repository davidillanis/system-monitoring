package org.system.monitoring.domain.collection.dto.request;


import org.system.monitoring.domain.collection.BonusCollection;
import org.system.monitoring.infrastructure.firebase.GenericRequest;

public record BonusRequestDTO(
        String nameBonus,
        String description,
        int requiredTickets,
        double status
) implements GenericRequest<BonusCollection> {
    @Override
    public BonusCollection toEntity() {
        return new BonusCollection(null, nameBonus, description, requiredTickets, status);
    }
}
