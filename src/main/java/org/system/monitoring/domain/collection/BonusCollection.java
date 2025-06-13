package org.system.monitoring.domain.collection;

import lombok.*;
import org.system.monitoring.infrastructure.firebase.GenericEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class BonusCollection implements GenericEntity {
    private String id;
    private String nameBonus;
    private String description;
    private int requiredTickets;
    private double status;
}
