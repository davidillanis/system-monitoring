package org.system.monitoring.domain.collection.dto.response;

import org.system.monitoring.domain.collection.TickedCollection;
import org.system.monitoring.infrastructure.firebase.DocRefAttribute;
import org.system.monitoring.infrastructure.firebase.GenericResponse;

import java.util.Date;

public class TicketResponseDTO implements GenericResponse<TickedCollection> {
    private String id;
    private Date exchangeDate;
    private Date creationDate;
    private boolean isValid;

    private DocRefAttribute residentRef;
    private DocRefAttribute bonusRef;
}
