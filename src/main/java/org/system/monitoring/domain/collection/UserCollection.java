package org.system.monitoring.domain.collection;

import lombok.*;
import org.system.monitoring.application.util.EStatus;
import org.system.monitoring.infrastructure.firebase.GenericEntity;
import org.system.monitoring.infrastructure.firebase.util.ERole;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserCollection implements GenericEntity {
    private String id;
    private String name;
    private String lastName;
    private String dni;
    private String email;
    private String phone;
    private String address;
    private String imageUrl;
    private Date registrationDate;
    private ERole role;
    private EStatus status;
}