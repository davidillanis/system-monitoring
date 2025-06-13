package org.system.monitoring.infrastructure.firebase.util;

import lombok.Getter;
import org.system.monitoring.domain.collection.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum ENameEntity {
    BONUS(BonusCollection.class.getSimpleName()),
    RESIDENT(ResidentCollection.class.getSimpleName()),
    TICKED(TickedCollection.class.getSimpleName()),
    USER(UserCollection.class.getSimpleName()),
    WORKER(WorkerCollection.class.getSimpleName());

    private final String value;

    ENameEntity(String role) {
        this.value = role;
    }

    public static List<String> getList(){
        return Arrays.stream(ERole.values()).map(Enum::name).collect(Collectors.toList());
    }

    public static ENameEntity fromValue(String value) {
        return Arrays.stream(ENameEntity.values()).filter(e -> e.value.equals(value)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No enum constant with value: " + value));
    }

}
