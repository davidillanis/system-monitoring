package org.system.monitoring.infrastructure.adapter;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.system.monitoring.domain.collection.dto.request.WorkerRequestDTO;
import org.system.monitoring.domain.collection.dto.response.WorkerResponseDTO;
import org.system.monitoring.infrastructure.firebase.DefaultResponse;
import org.system.monitoring.infrastructure.firebase.service.DocumentService;
import org.system.monitoring.infrastructure.firebase.util.ResponseStatusDTO;
import org.system.monitoring.infrastructure.firebase.util.UtilsController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(value = "/worker", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Worker Management", description = "APIs for managing workers like recyclers and operators")
public class WorkerController {
    private final Class<WorkerResponseDTO> className = WorkerResponseDTO.class;

    @Autowired
    private DocumentService documentService;

    /*@Operation(summary = "Create a worker", description = "Creates a new worker (operator or recycler) and stores their data.")
    @PostMapping("/create")
    public ResponseEntity<ResponseStatusDTO> create(@Valid @RequestBody WorkerRequestDTO obj,
                                                    BindingResult binding,
                                                    @RequestParam(required = false) String uid) {
        return UtilsController.save(binding, () -> documentService.create(obj, uid));
    }*/

    @Operation(summary = "List all workers", description = "Returns a paginated list of workers.")
    @GetMapping("/list")
    public CompletableFuture<ResponseEntity<?>> list(@RequestParam(defaultValue = "10", required = false) Integer limit) throws ExecutionException, InterruptedException {
        return UtilsController.list(documentService, new DefaultResponse(limit, className), className);
    }

    @Operation(summary = "Search for workers", description = "Searches workers using text-based input.")
    @GetMapping("/search")
    public CompletableFuture<ResponseEntity<?>> search(@RequestParam String search)  {
        if (search == null || search.trim().isEmpty()) {
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        }
        return UtilsController.search(documentService, new DefaultResponse(null, className), search, className);
    }

    @Operation(summary = "Get worker by ID", description = "Retrieves worker details using their UID.")
    @GetMapping("/byId/{uid}")
    public ResponseEntity<?> byId(@PathVariable String uid) {
        return UtilsController.byId(documentService, DefaultResponse.defaultByID(uid, className));
    }

    @Operation(summary = "Get unique values by field", description = "Returns all distinct values for a given field in the worker collection.")
    @GetMapping("/setValuesPerField/{field}")
    public CompletableFuture<ResponseEntity<?>> setValuesPerField(@PathVariable String field) {
        var defaultResponse = DefaultResponse.defaultByID(null, className);
        return UtilsController.setValuesPerField(documentService, defaultResponse, field);
    }

    @Operation(summary = "Update a worker", description = "Updates an existing worker's data.")
    @PutMapping("/update")
    public ResponseEntity<ResponseStatusDTO> update(@Valid @RequestBody WorkerRequestDTO obj,
                                                    BindingResult binding,
                                                    @RequestParam String uid) {
        var responseSupplier = DefaultResponse.defaultByID(uid, className);
        return UtilsController.save(binding, () -> documentService.update(responseSupplier, obj));
    }

    @Operation(summary = "Delete a worker", description = "Deletes a worker using their UID.")
    @DeleteMapping("/delete/{uid}")
    public ResponseEntity<ResponseStatusDTO> delete(@PathVariable String uid) {
        return UtilsController.delete(() -> documentService.delete(DefaultResponse.defaultByID(uid, className)));
    }
}
