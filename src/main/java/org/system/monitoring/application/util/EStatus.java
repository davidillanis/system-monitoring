package org.system.monitoring.application.util;

import org.system.monitoring.infrastructure.firebase.util.ERole;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum EStatus {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE"),
    SUSPENDED("SUSPENDED");

    private final String status;

    EStatus(String status) {
        this.status = status;
    }

    ERole getStatus() {
        return ERole.valueOf(status);
    }

    public static List<String> getList(){
        return Arrays.stream(ERole.values()).map(Enum::name).collect(Collectors.toList());
    }
}
