package org.system.monitoring.infrastructure.firebase;

import lombok.*;
import org.system.monitoring.infrastructure.firebase.util.ENameEntity;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DocRefAttribute{
    private ENameEntity entity;
    private String uidRef;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DocRefAttribute that = (DocRefAttribute) o;
        return entity == that.entity && Objects.equals(uidRef, that.uidRef);
    }

}
