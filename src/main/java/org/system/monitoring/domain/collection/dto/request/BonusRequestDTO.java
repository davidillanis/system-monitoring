package org.system.monitoring.domain.collection.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import org.system.monitoring.domain.collection.BonusCollection;
import org.system.monitoring.infrastructure.firebase.GenericRequest;

public record BonusRequestDTO(
        @NotBlank
        @Size(min = 3, max = 100)
        @Schema(description = "Bonus name", example = "Supermarket Discount")
        String nameBonus,

        @Size(max = 255)
        @Schema(description = "Description of the bonus", example = "Get 10% off on purchases over $50")
        String description,

        @Min(1)
        @Schema(description = "Number of tickets required to redeem this bonus", example = "10")
        int requiredTickets,

        @DecimalMin(value = "0.0")
        @DecimalMax(value = "1.0")
        @Schema(description = "Bonus status (e.g., 1.0 = active, 0.0 = inactive)", example = "1.0")
        double status

) implements GenericRequest<BonusCollection> {
    @Override
    public BonusCollection toEntity() {
        return new BonusCollection(null, nameBonus, description, requiredTickets, status);
    }
}
