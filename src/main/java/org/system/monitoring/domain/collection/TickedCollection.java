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
public class TickedCollection implements GenericEntity {
    private String id;
    private Date exchangeDate;
    private Date creationDate;
    private boolean isValid;

    private DocRefAttribute residentRef;
    private DocRefAttribute bonusRef;
}
