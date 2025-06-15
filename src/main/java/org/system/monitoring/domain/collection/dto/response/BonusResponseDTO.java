package org.system.monitoring.domain.collection.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.system.monitoring.domain.collection.BonusCollection;
import org.system.monitoring.infrastructure.firebase.GenericResponse;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BonusResponseDTO implements GenericResponse<BonusCollection> {
    private String id;
    private String nameBonus;
    private String description;
    private int requiredTickets;
    private double status;
}
