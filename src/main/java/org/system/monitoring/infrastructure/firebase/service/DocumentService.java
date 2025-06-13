package org.system.monitoring.infrastructure.firebase.service;


import com.google.cloud.firestore.Filter;
import org.system.monitoring.infrastructure.firebase.*;
//import org.ecommerce.application.dto.response.composite.DateIntervalDTO;
//import org.ecommerce.infraestructure.persistence.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;


public interface DocumentService {
    <T extends GenericEntity> String create(GenericRequest<T> obj, String... uid);
    <T extends GenericEntity & GenericRequest<?>> String create(DefaultRequest<T> requestDTO, String... uid);
    <T extends GenericResponse<?>> CompletableFuture<List<T>> list(DefaultResponse defaultResponse, Class<T> responseClass, String ...fields);
    <T extends GenericResponse<?>> CompletableFuture<List<T>> list(DefaultResponse defaultResponse, String ...fields);
    <T extends GenericResponse<?>> CompletableFuture<List<T>> list(DefaultResponse defaultResponse, Filter filter, Class<T> responseClass, String ...fields);
    <T extends GenericResponse<?>> CompletableFuture<List<T>> search(DefaultResponse defaultResponse, String searchTerm, Class<T> responseClass, String ...removedFields);
    CompletableFuture<List<Object>> setValuesPerField(DefaultResponse defaultResponse, String field);
    GenericEntity getById(DefaultResponse defaultResponse);
    <T extends GenericEntity> void update(DefaultResponse defaultResponse, GenericRequest<T> request);
    void delete(DefaultResponse defaultResponse);
}
