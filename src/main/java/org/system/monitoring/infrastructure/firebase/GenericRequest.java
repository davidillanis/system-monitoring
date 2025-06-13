package org.system.monitoring.infrastructure.firebase;

public interface GenericRequest<T extends GenericEntity> extends GenericEntity {
    T toEntity();
}
