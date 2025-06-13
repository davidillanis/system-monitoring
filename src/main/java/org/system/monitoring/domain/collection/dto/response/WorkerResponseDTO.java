package org.system.monitoring.domain.collection.dto.response;

import org.system.monitoring.domain.collection.WorkerCollection;
import org.system.monitoring.infrastructure.firebase.DocRefAttribute;
import org.system.monitoring.infrastructure.firebase.GenericResponse;

import java.util.Date;

public class WorkerResponseDTO implements GenericResponse<WorkerCollection> {
    private String id;
    private boolean isActive;
    private double salary;
    private Date dateOfHire;

    private DocRefAttribute userRef;
}
