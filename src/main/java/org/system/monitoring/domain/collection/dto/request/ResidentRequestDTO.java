package org.system.monitoring.domain.collection.dto.request;

import org.system.monitoring.domain.collection.ResidentCollection;
import org.system.monitoring.infrastructure.firebase.DocRefAttribute;
import org.system.monitoring.infrastructure.firebase.GenericRequest;
import org.system.monitoring.infrastructure.firebase.util.ENameEntity;

import java.util.Date;

public record ResidentRequestDTO(
        int totalTickets,
        boolean isActive,
        String userRef
) implements GenericRequest<ResidentCollection> {
    @Override
    public ResidentCollection toEntity() {
        return new ResidentCollection(null, totalTickets, isActive, new DocRefAttribute(ENameEntity.RESIDENT, userRef));
    }
}
