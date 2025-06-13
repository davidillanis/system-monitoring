package org.system.monitoring.domain.collection.dto.request;

import org.system.monitoring.domain.collection.WorkerCollection;
import org.system.monitoring.infrastructure.firebase.DocRefAttribute;
import org.system.monitoring.infrastructure.firebase.GenericRequest;
import org.system.monitoring.infrastructure.firebase.util.ENameEntity;

import java.util.Date;

public record WorkerRequestDTO(
        boolean isActive,
        double salary,
        Date dateOfHire,
        String userRef
) implements GenericRequest<WorkerCollection> {
    @Override
    public WorkerCollection toEntity() {
        return new WorkerCollection(null, isActive, salary, dateOfHire, new DocRefAttribute(ENameEntity.USER, userRef));
    }
}
