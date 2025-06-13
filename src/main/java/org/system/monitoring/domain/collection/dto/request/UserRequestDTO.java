package org.system.monitoring.domain.collection.dto.request;

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
        @NotBlank String name,
        String lastName,
        @Size(min = 8, max = 10) String dni,
        @Email String email,
        @Size(min = 9, max = 15) @Pattern(regexp = "\\d+") String phone,
        String address,
        String imageUrl,
        ERole role
) implements GenericRequest<UserCollection> {
    @Override
    public UserCollection toEntity() {
        return new UserCollection(null, name, lastName, dni, email, phone, address, imageUrl, new Date(), role, EStatus.ACTIVE);
    }
}
