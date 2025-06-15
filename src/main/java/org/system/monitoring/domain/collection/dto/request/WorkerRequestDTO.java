package org.system.monitoring.domain.collection.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.system.monitoring.domain.collection.WorkerCollection;
import org.system.monitoring.infrastructure.firebase.DocRefAttribute;
import org.system.monitoring.infrastructure.firebase.GenericRequest;
import org.system.monitoring.infrastructure.firebase.util.ENameEntity;

import java.util.Date;

public record WorkerRequestDTO(
        @Schema(description = "Indicates whether the worker is currently active", example = "true")
        @NotNull
        boolean isActive,

        @Schema(description = "Total weight of materials collected by the worker", example = "27.5")
        @NotNull
        @Min(0)
        double salary,

        @Schema(description = "Date of hiring", example = "2021-01-15")
        @NotNull
        Date dateOfHire,

        @Schema(description = "User ID associated with this worker", example = "abc123xyz")
        @NotNull
        String userRef
) implements GenericRequest<WorkerCollection> {
    @Override
    public WorkerCollection toEntity() {
        return new WorkerCollection(null, isActive, salary, dateOfHire, new DocRefAttribute(ENameEntity.USER, userRef));
    }
}
