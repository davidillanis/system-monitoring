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
public class ResidentCollection implements GenericEntity {
    private String id;
    private int totalTickets;
    private boolean isActive;

    private DocRefAttribute userRef;
}
