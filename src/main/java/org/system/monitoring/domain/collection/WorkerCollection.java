package org.system.monitoring.domain.collection;

import lombok.*;
import org.system.monitoring.infrastructure.firebase.DocRefAttribute;
import org.system.monitoring.infrastructure.firebase.GenericEntity;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class WorkerCollection implements GenericEntity {
    private String id;
    private boolean isActive;
    private double salary;
    private Date dateOfHire;

    private DocRefAttribute userRef;
}
