package org.system.monitoring.domain.collection.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.system.monitoring.domain.collection.WorkerCollection;
import org.system.monitoring.infrastructure.firebase.DocRefAttribute;
import org.system.monitoring.infrastructure.firebase.GenericResponse;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WorkerResponseDTO implements GenericResponse<WorkerCollection> {
    private String id;
    private boolean isActive;
    private double salary;
    private Date dateOfHire;

    private DocRefAttribute userRef;
}
