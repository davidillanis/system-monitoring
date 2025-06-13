package org.system.monitoring.domain.collection.dto.request;

import org.system.monitoring.domain.collection.TickedCollection;
import org.system.monitoring.infrastructure.firebase.DocRefAttribute;
import org.system.monitoring.infrastructure.firebase.GenericRequest;
import org.system.monitoring.infrastructure.firebase.util.ENameEntity;

import java.util.Date;

public record TicketRequestDTO(
        Date exchangeDate,
        boolean isValid,
        String residentRef,
        String bonusRef
) implements GenericRequest<TickedCollection> {
    @Override
    public TickedCollection toEntity() {
        return new TickedCollection(null, exchangeDate, new Date(), isValid, new DocRefAttribute(ENameEntity.RESIDENT, residentRef), new DocRefAttribute(ENameEntity.BONUS, bonusRef));
    }
}
