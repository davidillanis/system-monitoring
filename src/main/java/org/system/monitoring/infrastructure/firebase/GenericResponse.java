package org.system.monitoring.infrastructure.firebase;



import java.lang.reflect.Field;
import java.util.Arrays;

public interface GenericResponse<T extends GenericEntity> {
    default String[] listAttributes(){
        Field[] fields = getClass().getDeclaredFields();
        return Arrays.stream(fields).map(Field::getName).toArray(String[]::new);
    }
}