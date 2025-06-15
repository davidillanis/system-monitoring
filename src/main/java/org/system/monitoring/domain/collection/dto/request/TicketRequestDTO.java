package org.system.monitoring.domain.collection.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.system.monitoring.domain.collection.TickedCollection;
import org.system.monitoring.infrastructure.firebase.DocRefAttribute;
import org.system.monitoring.infrastructure.firebase.GenericRequest;
import org.system.monitoring.infrastructure.firebase.util.ENameEntity;

import java.util.Date;

public record TicketRequestDTO(
        @NotNull
        @Schema(description = "Date when the ticket is exchanged", example = "2025-06-14T10:00:00Z")
        Date exchangeDate,

        @Schema(description = "Indicates if the ticket is still valid", example = "true")
        boolean valid,

        @NotBlank
        @Schema(description = "Reference ID of the associated resident", example = "abc123")
        String residentRef,

        @NotBlank
        @Schema(description = "Reference ID of the associated bonus", example = "bonus789")
        String bonusRef
) implements GenericRequest<TickedCollection> {
    @Override
    public TickedCollection toEntity() {
        return new TickedCollection(null, exchangeDate, new Date(), valid, new DocRefAttribute(ENameEntity.RESIDENT, residentRef), new DocRefAttribute(ENameEntity.BONUS, bonusRef));
    }
}
