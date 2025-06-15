package org.system.monitoring.domain.collection.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.system.monitoring.application.util.EStatus;
import org.system.monitoring.domain.collection.UserCollection;
import org.system.monitoring.infrastructure.firebase.GenericRequest;
import org.system.monitoring.infrastructure.firebase.util.ERole;

import java.util.Date;

public record UserRequestDTO (
        @NotBlank
        @Schema(description = "User's first name", example = "Juan")
        String name,
        @Schema(description = "User's last name", example = "Pérez")
        String lastName,
        @Size(min = 8, max = 10)
        @Schema(description = "National ID (DNI) of the user", example = "12345678")
        String dni,
        @Email
        @Schema(description = "User's email address", example = "juan.perez@example.com")
        String email,
        @Size(min = 9, max = 15)
        @Pattern(regexp = "\\d+")
        @Schema(description = "User's phone number (digits only)", example = "987654321")
        String phone,
        @Schema(description = "User's address", example = "Av. Siempre Viva 123")
        String address,
        @Schema(description = "URL to the user's profile image", example = "https://example.com/images/profile.jpg")
        String imageUrl,
        @Schema(description = "Role assigned to the user", example = "RESIDENT",  allowableValues = {"RESIDENT", "RECYCLER", "OPERATOR", "ADMIN"})
        ERole role,
        @Schema(description = "Current status of the user", example = "ACTIVE" , allowableValues = {"ACTIVE", "INACTIVE", "SUSPENDED"})
        EStatus status
) implements GenericRequest<UserCollection> {
    @Override
    public UserCollection toEntity() {
        return new UserCollection(null, name, lastName, dni, email, phone, address, imageUrl, new Date(), role, status);
    }
}
