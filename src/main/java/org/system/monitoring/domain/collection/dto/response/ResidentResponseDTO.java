package org.system.monitoring.domain.collection.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.system.monitoring.domain.collection.ResidentCollection;
import org.system.monitoring.infrastructure.firebase.DocRefAttribute;
import org.system.monitoring.infrastructure.firebase.GenericResponse;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResidentResponseDTO implements GenericResponse<ResidentCollection> {
    private String id;
    private int totalTickets;
    private boolean isActive;

    private DocRefAttribute userRef;
}
