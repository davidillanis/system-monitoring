package org.system.monitoring.infrastructure.firebase;

import lombok.*;
import org.system.monitoring.infrastructure.firebase.util.ENameEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DocRefAttribute{
    private ENameEntity entity;
    private String uidRef;
}
