package org.system.monitoring.domain.collection.dto.response;

import org.system.monitoring.domain.collection.ResidentCollection;
import org.system.monitoring.infrastructure.firebase.DocRefAttribute;
import org.system.monitoring.infrastructure.firebase.GenericResponse;

import java.util.Date;

public class ResidentResponseDTO implements GenericResponse<ResidentCollection> {
    private String id;
    private int totalTickets;
    private boolean isActive;

    private DocRefAttribute userRef;
}
