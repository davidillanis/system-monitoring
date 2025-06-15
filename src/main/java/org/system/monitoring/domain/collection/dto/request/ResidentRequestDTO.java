package org.system.monitoring.domain.collection.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.system.monitoring.domain.collection.ResidentCollection;
import org.system.monitoring.infrastructure.firebase.DocRefAttribute;
import org.system.monitoring.infrastructure.firebase.GenericRequest;
import org.system.monitoring.infrastructure.firebase.util.ENameEntity;

import java.util.Date;

public record ResidentRequestDTO(
        @Schema(description = "Total number of tickets the resident has earned", example = "15")
        @Min(0)
        int totalTickets,

        @Schema(description = "Indicates whether the resident is currently active", example = "true")
        @NotNull
        boolean isActive,

        @Schema(description = "UID reference to the associated user entity", example = "abc123xyz456")
        @NotNull
        String userRef
) implements GenericRequest<ResidentCollection> {
    @Override
    public ResidentCollection toEntity() {
        return new ResidentCollection(null, totalTickets, isActive, new DocRefAttribute(ENameEntity.RESIDENT, userRef));
    }
}
