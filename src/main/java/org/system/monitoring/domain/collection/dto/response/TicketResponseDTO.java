package org.system.monitoring.domain.collection.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.system.monitoring.domain.collection.TickedCollection;
import org.system.monitoring.infrastructure.firebase.DocRefAttribute;
import org.system.monitoring.infrastructure.firebase.GenericResponse;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TicketResponseDTO implements GenericResponse<TickedCollection> {
    private String id;
    private Date exchangeDate;
    private Date creationDate;
    private boolean valid;

    private DocRefAttribute residentRef;
    private DocRefAttribute bonusRef;
}
