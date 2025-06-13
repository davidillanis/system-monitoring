package org.system.monitoring.infrastructure.firebase.service.implementation;


import com.google.cloud.firestore.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.system.monitoring.infrastructure.firebase.*;
import org.system.monitoring.infrastructure.firebase.configuration.FirebaseConfig;
import org.system.monitoring.infrastructure.firebase.service.DocumentService;
import org.system.monitoring.infrastructure.firebase.util.ENameEntity;
import org.system.monitoring.infrastructure.firebase.util.MapperObject;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {
    private final FirebaseConfig firebaseConfig;
    private final GenericRepository genericRepository;

    @Override
    public <T extends GenericEntity> String create(GenericRequest<T> obj, String... uid) {
        T entity = obj.toEntity();
        Map<String, Object> docData = MapperObject.toMap(entity);
        docData.remove("id");
        return genericRepository.create(getCollection(entity.collectionName(), null, null), docData, uid);
    }

    @Override
    public <T extends GenericEntity & GenericRequest<?>> String create(DefaultRequest<T> requestDTO, String... uid) {
        T entity = requestDTO.toEntity();
        GenericEntity genericEntity = entity.toEntity();
        Map<String, Object> docData = MapperObject.toMap(genericEntity);
        CollectionReference collection = getCollection(genericEntity.collectionName(), requestDTO.getParentCollection(), requestDTO.getParentUID());
        return genericRepository.create(collection, docData, uid);
    }

    @Override
    public <T extends GenericResponse<?>> CompletableFuture<List<T>> list(DefaultResponse defaultResponse, String ...fields) {
        Class<T> responseClass = (Class<T>) defaultResponse.getResponseClass().getClass();
        return listEntities(defaultResponse, responseClass, fields);
    }

    @Override
    public <T extends GenericResponse<?>> CompletableFuture<List<T>> list(DefaultResponse defaultResponse, Class<T> responseClass, String ...fields) {
        return listEntities(defaultResponse, responseClass, fields);
    }

    @Override
    public <T extends GenericResponse<?>> CompletableFuture<List<T>> list(DefaultResponse defaultResponse, Filter filter, Class<T> responseClass, String ...fields) {
        fields=fields.length==0?defaultResponse.listAttributes():fields;
        return genericRepository.list(getCollection(defaultResponse), defaultResponse.getLimit(), filter, fields)
                .thenApply(list -> list.stream()
                        .map(map -> MapperObject.toEntity(map, defaultResponse.getResponseClass().getClass()))
                        .map(responseClass::cast)
                        .collect(Collectors.toList()));
    }

    @Override
    public <T extends GenericResponse<?>> CompletableFuture<List<T>> search(DefaultResponse defaultResponse, String searchTerm, Class<T> responseClass, String... removedFields) {
        String normalizedSearch = searchTerm.trim().toLowerCase();
        Set<String> excludedFields = new HashSet<>(Arrays.asList(removedFields));

        List<String> searchableAttributes = Arrays.stream(defaultResponse.listAttributes())
                .filter(attr -> !excludedFields.contains(attr) || attr.equals("id")).toList();

        return list(defaultResponse, responseClass).thenApply(list -> list.stream()
                .filter(item -> {
                    Map<String, Object> fieldMap = MapperObject.toMap(item);
                    return searchableAttributes.stream().anyMatch(attr -> {
                        Object value = fieldMap.get(attr);
                        return value != null && value.toString().toLowerCase().contains(normalizedSearch);
                    });
                }).collect(Collectors.toList()));
    }

    @Override
    public CompletableFuture<List<Object>> setValuesPerField(DefaultResponse defaultResponse, String field) {
        return genericRepository.setValuesPerField(getCollection(defaultResponse), field)
                .thenApply(ArrayList::new);
    }

    @Override
    public GenericEntity getById(DefaultResponse defaultResponse) {
        return genericRepository.getById(getCollection(defaultResponse), defaultResponse.getUid())
                .map(map -> MapperObject.toEntity(map, defaultResponse.getEntityInstance().getClass()))
                .orElseThrow(() -> new RuntimeException("El dato solicitado no existe"));
    }

    @Override
    public <T extends GenericEntity> void update(DefaultResponse defaultResponse, GenericRequest<T> request) {
        genericRepository.update(getCollection(defaultResponse), defaultResponse.getUid(), MapperObject.toMap(request));
    }

    @Override
    public void delete(DefaultResponse defaultResponse) {
        genericRepository.delete(getCollection(defaultResponse), defaultResponse.getUid());
    }

    //other methods
    private <T extends GenericResponse<?>> CompletableFuture<List<T>> listEntities(DefaultResponse defaultResponse, Class<T> responseClass, String ...fields) {
        fields=fields.length==0?defaultResponse.listAttributes():fields;
        return genericRepository.list(getCollection(defaultResponse), defaultResponse.getLimit(), fields)
                .thenApply(list -> list.stream()
                        .map(map -> MapperObject.toEntity(map, defaultResponse.getResponseClass().getClass()))
                        .map(responseClass::cast)
                        .collect(Collectors.toList()));
    }

    private CollectionReference getCollection(String collectionName, ENameEntity parentCollection, String parentUID) {
        Firestore firestore = firebaseConfig.getFirestore();
        if (parentCollection != null && parentUID != null) {
            return firestore.collection(parentCollection.getValue()).document(parentUID).collection(collectionName);
        }
        return firestore.collection(collectionName);
    }

    private CollectionReference getCollection(DefaultResponse defaultResponse) {
        Objects.requireNonNull(defaultResponse, "defaultResponse cannot be null");

        if (defaultResponse.getDefaultResponse() == null) {
            return firebaseConfig.getFirestore().collection(defaultResponse.collectionName());
        }

        Firestore firestore = firebaseConfig.getFirestore();
        Stack<String[]> stack = new Stack<>();

        for (DefaultResponse current = defaultResponse; current != null; current = current.getDefaultResponse()) {
            stack.push(new String[]{current.collectionName(), current.getUid()});
        }

        DocumentReference docRef = null;
        while (!stack.isEmpty()) {
            String[] data = stack.pop();
            if (docRef == null) {
                docRef = firestore.collection(data[0]).document(data[1]);
            } else if (stack.isEmpty()) {
                return docRef.collection(data[0]);
            } else {
                docRef = docRef.collection(data[0]).document(data[1]);
            }
        }

        return firestore.collection(defaultResponse.collectionName());
    }
}
