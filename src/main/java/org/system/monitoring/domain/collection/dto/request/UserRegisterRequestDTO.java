package org.system.monitoring.domain.collection.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.system.monitoring.infrastructure.firebase.util.ERole;

@Data
@Schema(description = "Request body for registering a new user")
public class UserRegisterRequestDTO {

    @NotBlank
    @Schema(description = "User's first name", example = "María")
    private String name;

    @NotBlank
    @Schema(description = "User's last name", example = "González")
    private String lastName;

    @NotBlank
    @Schema(description = "National ID (DNI) of the user", example = "87654321")
    private String dni;

    @NotBlank
    @Schema(description = "User's email address", example = "maria.gonzalez@example.com")
    private String email;

    @NotBlank
    @Schema(description = "User's phone number", example = "987654321")
    private String phone;

    @NotBlank
    @Schema(description = "User's address", example = "Calle Falsa 123")
    private String address;

    @Schema(description = "URL of the user's profile picture", example = "https://example.com/avatar.jpg")
    private String imageUrl;

    @NotNull
    @Schema(description = "Role to assign to the user", example = "RESIDENT", allowableValues = {"RESIDENT", "RECYCLER", "OPERATOR", "ADMIN"})
    private ERole role;
}
