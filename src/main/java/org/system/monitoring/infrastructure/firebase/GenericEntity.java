package org.system.monitoring.infrastructure.firebase;

public interface GenericEntity {
    default String collectionName() {
        return getClass().getSimpleName();
    }
}
