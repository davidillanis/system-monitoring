package org.system.monitoring.domain.collection.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.system.monitoring.domain.collection.UserCollection;
import org.system.monitoring.infrastructure.firebase.GenericResponse;
import org.system.monitoring.infrastructure.firebase.util.ERole;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserResponseDTO implements GenericResponse<UserCollection> {
    private String id;
    private String name;
    private String lastName;
    private String email;
    private String phone;
    private ERole role;
    private String imageUrl;
}
