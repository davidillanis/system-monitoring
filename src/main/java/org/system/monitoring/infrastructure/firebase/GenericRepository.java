package org.system.monitoring.infrastructure.firebase;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.system.monitoring.infrastructure.firebase.configuration.FirebaseConfig;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Repository
public class GenericRepository {
    @Autowired
    private FirebaseConfig firebaseConfig;

    public CompletableFuture<List<Map<String, Object>>> list(CollectionReference collectionReference, Integer limit, String... fields) {
        return list(collectionReference, limit, null, fields);
    }

    public CompletableFuture<List<Map<String, Object>>> list(CollectionReference collectionReference, Integer limit, Filter filter, String... fields) {
        Query collection = fields.length > 0 ? collectionReference.select(fields) : collectionReference;

        if (filter != null) {
            collection = collection.where(filter);
        }

        Query finalCollection = limit != null ? collection.limit(limit) : collection;

        return CompletableFuture.supplyAsync(() -> {
            try {
                List<QueryDocumentSnapshot> documents = finalCollection.get().get().getDocuments();
                return documents.stream().map(data -> {
                    Map<String, Object> dataMap = data.getData();
                    dataMap.put("id", data.getId());
                    return dataMap;
                }).toList();
            } catch (InterruptedException | ExecutionException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Error fetching documents ", e);
            }
        });
    }

    public CompletableFuture<Set<Object>> setValuesPerField(CollectionReference collectionReference, String field) {

        return CompletableFuture.supplyAsync(() -> {
            try {
                List<QueryDocumentSnapshot> documents = collectionReference.get().get().getDocuments();

                return documents.stream()
                        .map(doc -> doc.get(field))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet());
            } catch (InterruptedException | ExecutionException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Error fetching documents ", e);
            }
        });
    }

    public Optional<Map<String, Object>> getById(CollectionReference collectionReference, String id) {
        try {
            DocumentReference docRef = collectionReference.document(id);
            DocumentSnapshot document = docRef.get().get();

            if (document.exists()) {
                Map<String, Object> data = document.getData();
                if (data != null) {
                    data.put("id", document.getId());
                }
                return Optional.ofNullable(data);
            } else {
                return Optional.empty();
            }
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Error fetching document by ID", e);
        }
    }

    public String create(CollectionReference collectionReference, Map<String, Object> docData, String... uid) {
        try {
            docData=GenericRepositoryUtils.convertDocRefAttributes(docData, firebaseConfig);

            DocumentReference docRef = (uid.length > 0 && uid[0] != null && !uid[0].isEmpty())
                    ? collectionReference.document(uid[0]) : collectionReference.document();

            ApiFuture<WriteResult> writeResultApiFuture = docRef.create(docData);
            writeResultApiFuture.get();
            return docRef.getId();
        } catch (Exception e) {
            throw new RuntimeException("Error creating document", e);
        }
    }

    public void update(CollectionReference collectionReference, String id, Map<String, Object> docData) {
        docData=GenericRepositoryUtils.convertDocRefAttributes(docData, firebaseConfig);
        ApiFuture<WriteResult> writeResultApiFuture = collectionReference.document(id).update(docData);
        try {
            writeResultApiFuture.get();
        } catch (Exception e) {
            throw new RuntimeException("Error updating document", e);
        }
    }

    public void delete(CollectionReference collectionReference, String id) {
        DocumentReference documentReference = collectionReference.document(id);
        try {
            if (documentReference.get().get().exists()) {
                documentReference.delete().get();
            } else {
                throw new RuntimeException("Document: " + id + " does not exist");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error deleting document"+ e.getMessage());
        }

    }

}