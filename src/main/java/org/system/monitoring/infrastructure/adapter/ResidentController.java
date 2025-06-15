package org.system.monitoring.infrastructure.adapter;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.system.monitoring.domain.collection.dto.request.ResidentRequestDTO;
import org.system.monitoring.domain.collection.dto.response.ResidentResponseDTO;
import org.system.monitoring.infrastructure.firebase.DefaultResponse;
import org.system.monitoring.infrastructure.firebase.service.DocumentService;
import org.system.monitoring.infrastructure.firebase.util.ResponseStatusDTO;
import org.system.monitoring.infrastructure.firebase.util.UtilsController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(value = "/resident", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Resident Controller", description = "Manages resident-specific operations")
public class ResidentController {
    private final Class<ResidentResponseDTO> className = ResidentResponseDTO.class;

    @Autowired
    private DocumentService documentService;

    /*@Operation(summary = "Create a resident", description = "Registers a new resident and stores their information.")
    @PostMapping("/create")
    public ResponseEntity<ResponseStatusDTO> create(@Valid @RequestBody ResidentRequestDTO obj,
                                                    BindingResult binding,
                                                    @RequestParam(required = false) String uid) {
        return UtilsController.save(binding, () -> documentService.create(obj, uid));
    }*/

    @Operation(summary = "List all residents", description = "Returns a paginated list of residents.")
    @GetMapping("/list")
    public CompletableFuture<ResponseEntity<?>> list(@RequestParam(defaultValue = "10", required = false) Integer limit) throws ExecutionException, InterruptedException {
        return UtilsController.list(documentService, new DefaultResponse(limit, className), className);
    }

    @Operation(summary = "Search residents", description = "Searches for residents using a text query.")
    @GetMapping("/search")
    public CompletableFuture<ResponseEntity<?>> search(@RequestParam String search)  {
        if (search == null || search.trim().isEmpty()) {
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        }
        return UtilsController.search(documentService, new DefaultResponse(null, className), search, className);
    }

    @Operation(summary = "Get resident by ID", description = "Retrieves resident details by their UID.")
    @GetMapping("/byId/{uid}")
    public ResponseEntity<?> byId(@PathVariable String uid) {
        return UtilsController.byId(documentService, DefaultResponse.defaultByID(uid, className));
    }

    @Operation(summary = "Get field values across residents", description = "Retrieves all unique values for a specified field.")
    @GetMapping("/setValuesPerField/{field}")
    public CompletableFuture<ResponseEntity<?>> setValuesPerField(@PathVariable String field) {
        var defaultResponse = DefaultResponse.defaultByID(null, className);
        return UtilsController.setValuesPerField(documentService, defaultResponse, field);
    }

    @Operation(summary = "Update a resident", description = "Updates an existing resident’s information.")
    @PutMapping("/update")
    public ResponseEntity<ResponseStatusDTO> update(@Valid @RequestBody ResidentRequestDTO obj,
                                                    BindingResult binding,
                                                    @RequestParam String uid) {
        var responseSupplier = DefaultResponse.defaultByID(uid, className);
        return UtilsController.save(binding, () -> documentService.update(responseSupplier, obj));
    }

    @Operation(summary = "Delete a resident", description = "Deletes a resident by their UID.")
    @DeleteMapping("/delete/{uid}")
    public ResponseEntity<ResponseStatusDTO> delete(@PathVariable String uid) {
        return UtilsController.delete(() -> documentService.delete(DefaultResponse.defaultByID(uid, className)));
    }
}
