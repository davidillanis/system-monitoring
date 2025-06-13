package org.system.monitoring.infrastructure.firebase.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum ERole {
    ADMIN("ADMIN"),
    OPERATOR("OPERATOR"),
    RESIDENT("RESIDENT"),
    RECYCLER("RECYCLER");

    private final String role;

    ERole(String role) {
        this.role = role;
    }

    ERole getRole() {
        return ERole.valueOf(role);
    }

    public static List<String> getList(){
        return Arrays.stream(ERole.values()).map(Enum::name).collect(Collectors.toList());
    }
}
