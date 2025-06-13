package org.system.monitoring.infrastructure.firebase;

import lombok.Getter;
import lombok.ToString;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@Getter
@ToString
public class DefaultResponse {
    private final GenericResponse<?> responseClass;//this clas the instance of the response value in children class
    private final GenericEntity entityInstance;
    private final String uid;
    private final DefaultResponse defaultResponse;
    private final Integer limit;

    public DefaultResponse(Integer limit, Class<? extends GenericResponse<?>> responseClass) {
        this(null, limit, null, responseClass);
    }
    public DefaultResponse(String uid, Integer limit, DefaultResponse defaultResponse, Class<? extends GenericResponse<?>> responseClass) {
        try {
            this.responseClass = (GenericResponse<?>) responseClass.getDeclaredConstructor().newInstance();
            this.entityInstance = genericEntity(responseClass);
            this.uid = uid;
            this.defaultResponse = defaultResponse;
            this.limit = limit;
        } catch (Exception e) {
            throw new RuntimeException("Error in instance in class ", e);
        }
    }

    private GenericEntity genericEntity(Class<?> responseClass) {
        try {
            Type[] genericInterfaces = responseClass.getGenericInterfaces();

            for (Type type : genericInterfaces) {
                if (type instanceof ParameterizedType parameterizedType) {
                    if (parameterizedType.getRawType() instanceof Class<?> rawInterface &&
                            GenericResponse.class.isAssignableFrom(rawInterface)) {

                        Type actualType = parameterizedType.getActualTypeArguments()[0];
                        if (actualType instanceof Class<?> entityClass) {
                            return (GenericEntity) entityClass.getDeclaredConstructor().newInstance();
                        }
                    }
                }
            }

            throw new RuntimeException("Error, dosen't as type entity extends Response");
        } catch (Exception e) {
            throw new RuntimeException("Error save a entity", e);
        }
    }

    public String[] listAttributes() {
        return responseClass.listAttributes();
    }

    public String collectionName() {
        return entityInstance.collectionName();
    }


    public static DefaultResponse defaultSupCollectionByID(String uid, DefaultResponse defaultResponse, Class<? extends GenericResponse<?>> responseClass){
        return new DefaultResponse(uid, null, defaultResponse, responseClass);
    }

    public static DefaultResponse defaultByID(String uid, Class<? extends GenericResponse<?>> responseClass){
        return new DefaultResponse(uid, null, null, responseClass);
    }

    public static DefaultResponse defaultBySupCollectionList(int limit, DefaultResponse defaultResponse, Class<? extends GenericResponse<?>> responseClass){
        return new DefaultResponse(null, limit, defaultResponse, responseClass);
    }

}
