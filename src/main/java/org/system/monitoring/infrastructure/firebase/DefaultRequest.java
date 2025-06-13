package org.system.monitoring.infrastructure.firebase;

import lombok.Getter;
import org.system.monitoring.infrastructure.firebase.util.ENameEntity;

public class DefaultRequest<T extends GenericRequest<?> & GenericEntity> implements GenericRequest<T> {

    private final T entity;
    @Getter
    private ENameEntity parentCollection;
    @Getter
    private String parentUID;

    public DefaultRequest(T entity) {
        this.entity = entity;
    }

    public DefaultRequest(T entity, ENameEntity parentCollection, String parentUID) {
        this.entity = entity;
        this.parentCollection = parentCollection;
        this.parentUID = parentUID;
    }

    @Override
    public T toEntity() {
        return entity;
    }
}
