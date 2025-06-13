package org.system.monitoring.infrastructure.firebase.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.firestore.DocumentReference;
import org.system.monitoring.infrastructure.firebase.DocRefAttribute;

import java.util.Map;
import java.util.Objects;

public final class MapperObject {
    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
    }

    private MapperObject() {}

    public static Map<String, Object> toMap(Object user) {
        Objects.requireNonNull(user, "this user cannot be null");
        return objectMapper.convertValue(user, Map.class);
    }

    public static <T> T toEntity(Map<String, Object> docData, Class<T> type) {
        Objects.requireNonNull(docData, "docData cannot be null");
        Objects.requireNonNull(type, "type cannot be null");

        docData.keySet().forEach(key -> {
            Object ref = docData.get(key);
            if (ref instanceof DocumentReference docRef) {
                docData.put(key, new DocRefAttribute(ENameEntity.fromValue(docRef.getParent().getId()), docRef.getId()));
            }
        });

        return objectMapper.convertValue(docData, type);
    }
}