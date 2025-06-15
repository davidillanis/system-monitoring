package org.system.monitoring.infrastructure.firebase.util;

import com.google.cloud.firestore.Filter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.system.monitoring.infrastructure.firebase.DefaultResponse;
import org.system.monitoring.infrastructure.firebase.GenericEntity;
import org.system.monitoring.infrastructure.firebase.GenericResponse;
import org.system.monitoring.infrastructure.firebase.service.DocumentService;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class UtilsController {
    private UtilsController() {}

    public static ResponseEntity<ResponseStatusDTO> save(BindingResult binding, Supplier<String> supplier){
        if(binding.hasErrors()) {
            return ResponseEntity.badRequest().body(ResponseStatusDTO.responseBinding(binding, EMessage.ERROR_VALIDATION));
        }
        try {
            String uid = supplier.get();
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ResponseStatusDTO.getInstance(true, EMessage.SUCCESSFUL, null, uid));
        }catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseStatusDTO
                    .getInstance(false, EMessage.INTERNAL_SERVER_ERROR, List.of(e.getMessage().split(","))));
        }
    }

    public static ResponseEntity<ResponseStatusDTO> save(BindingResult binding, Runnable runnable){
        if(binding.hasErrors()) {
            return ResponseEntity.badRequest().body(ResponseStatusDTO.responseBinding(binding, EMessage.ERROR_VALIDATION));
        }
        try {
            runnable.run();
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ResponseStatusDTO.getInstance(true, EMessage.SUCCESSFUL, null));
        }catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseStatusDTO
                    .getInstance(false, EMessage.INTERNAL_SERVER_ERROR, List.of(e.getMessage().split(","))));
        }
    }

    /*public static <T extends GenericResponse<?>> CompletableFuture<ResponseEntity<?>> list(DocumentService documentService,
                                                                                           DefaultResponse defaultResponse,
                                                                                           Class<T> responseClass) {
        return documentService.list(defaultResponse, responseClass)
                .thenApply(list -> list.isEmpty()? ResponseEntity.noContent().build(): ResponseEntity.ok(list))
                .exceptionally(e -> ResponseEntity.internalServerError().build());
    }

    public static <T extends GenericResponse<?>> CompletableFuture<ResponseEntity<?>> list(DocumentService documentService,
                                                                                           DefaultResponse defaultResponse,
                                                                                           Class<T> responseClass,
                                                                                           Filter filter) {
        return documentService.list(defaultResponse, filter, responseClass)
                .thenApply(list -> list.isEmpty()? ResponseEntity.noContent().build(): ResponseEntity.ok(list))
                .exceptionally(e -> ResponseEntity.internalServerError().build());
    }*/
    private static <T extends GenericResponse<?>> CompletableFuture<ResponseEntity<?>> list(Supplier<CompletableFuture<List<T>>> listSupplier) {

        return listSupplier.get()
                .thenApply(list -> list.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(list))
                .exceptionally(e -> ResponseEntity.internalServerError().build());
    }

    public static <T extends GenericResponse<?>> CompletableFuture<ResponseEntity<?>> list(DocumentService documentService,
                                                                                           DefaultResponse defaultResponse,
                                                                                           Class<T> responseClass) {
        return list(() -> documentService.list(defaultResponse, responseClass));
    }

    public static <T extends GenericResponse<?>> CompletableFuture<ResponseEntity<?>> list(DocumentService documentService,
                                                                                           DefaultResponse defaultResponse,
                                                                                           Class<T> responseClass,
                                                                                           Filter filter) {
        return list(() -> documentService.list(defaultResponse, filter, responseClass));
    }

    public static <T extends GenericResponse<?>> CompletableFuture<ResponseEntity<?>> search(DocumentService documentService,
                                                                                             DefaultResponse defaultResponse,
                                                                                             String searchTerm,
                                                                                             Class<T> responseClass,
                                                                                             String ...fieldsRemoved) {
        return documentService.search(defaultResponse, searchTerm, responseClass, fieldsRemoved)
                .thenApply(bills -> bills.isEmpty()? ResponseEntity.noContent().build(): ResponseEntity.ok(bills))
                .exceptionally(e -> ResponseEntity.internalServerError().build());
    }

    public static <T extends GenericEntity> ResponseEntity<?> byId(DocumentService documentService, DefaultResponse defaultResponse){
        try {
            return ResponseEntity.ok(documentService.getById(defaultResponse));
        }catch(Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseStatusDTO
                    .getInstance(false, EMessage.RESOURCE_NOT_FOUND, List.of(e.getMessage().split(","))));
        }
    }

    public static <T> CompletableFuture<ResponseEntity<?>> setValuesPerField(DocumentService documentService,
                                                                             DefaultResponse defaultResponse,
                                                                             String field){
        return documentService.setValuesPerField(defaultResponse, field)
                .thenApply(bills -> bills.isEmpty()? ResponseEntity.noContent().build(): ResponseEntity.ok(bills))
                .exceptionally(e -> ResponseEntity.internalServerError().build());
    }

    public static ResponseEntity<ResponseStatusDTO> delete(Runnable runnable){
        try {
            runnable.run();
            return ResponseEntity.ok(ResponseStatusDTO.getInstance(true, EMessage.SUCCESSFUL, null));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseStatusDTO
                    .getInstance(false, EMessage.RESOURCE_NOT_FOUND, List.of(e.getMessage().split(","))));
        }
    }

}
