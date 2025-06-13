package org.system.monitoring.infrastructure.firebase;


import org.system.monitoring.infrastructure.firebase.configuration.FirebaseConfig;
import org.system.monitoring.infrastructure.firebase.util.ENameEntity;

import java.util.Map;
import java.util.stream.Collectors;

public final class GenericRepositoryUtils {
    private GenericRepositoryUtils() {}

    /*public static Map<String, Object> convertDocRefAttributes(Map<String, Object> originalMap, FirebaseConfig firebaseConfig) {
        return originalMap.entrySet().stream().map(entry -> {
            if (entry.getValue() instanceof Map<?, ?> map) {
                if (map.containsKey("entity") && map.containsKey("uidRef")) {
                    String collection = ENameEntity.valueOf(map.get("entity").toString()).getValue();
                    String uidRef = map.get("uidRef").toString();
                    return Map.entry(entry.getKey(), firebaseConfig.getFirestore().collection(collection).document(uidRef));
                }
            }
            return entry;
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }*/

    public static Map<String, Object> convertDocRefAttributes(Map<String, Object> originalMap, FirebaseConfig firebaseConfig) {
        return originalMap.entrySet().stream()
                .filter(entry -> entry.getValue() != null).map(entry -> {
                    if (entry.getValue() instanceof Map<?, ?> map && map != null) {
                        Object entityObj = map.get("entity");
                        Object uidRefObj = map.get("uidRef");

                        if (entityObj != null && uidRefObj != null) {
                            try {
                                String collection = ENameEntity.valueOf(entityObj.toString()).getValue();
                                String uidRef = uidRefObj.toString();
                                return Map.entry(entry.getKey(), firebaseConfig.getFirestore().collection(collection).document(uidRef));
                            } catch (IllegalArgumentException e) {
                                return entry;
                            }
                        }
                    }
                    return entry;
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v1));
    }
}
