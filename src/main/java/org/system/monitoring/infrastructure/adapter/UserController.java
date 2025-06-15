package org.system.monitoring.infrastructure.adapter;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.system.monitoring.application.util.EStatus;
import org.system.monitoring.domain.collection.dto.request.ResidentRequestDTO;
import org.system.monitoring.domain.collection.dto.request.UserRegisterRequestDTO;
import org.system.monitoring.domain.collection.dto.request.UserRequestDTO;
import org.system.monitoring.domain.collection.dto.request.WorkerRequestDTO;
import org.system.monitoring.domain.collection.dto.response.UserResponseDTO;
import org.system.monitoring.infrastructure.firebase.DefaultResponse;
import org.system.monitoring.infrastructure.firebase.GenericRequest;
import org.system.monitoring.infrastructure.firebase.service.DocumentService;
import org.system.monitoring.infrastructure.firebase.util.ERole;
import org.system.monitoring.infrastructure.firebase.util.ResponseStatusDTO;
import org.system.monitoring.infrastructure.firebase.util.UtilsController;

import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "User Management", description = "Operations related to users, residents, and workers")
public class UserController {
    private final Class<UserResponseDTO> className = UserResponseDTO.class;

    @Autowired
    private DocumentService documentService;

    @Operation(summary = "Create basic user", description = "Creates a new user with general attributes")
    @PostMapping("/create")
    public ResponseEntity<ResponseStatusDTO> create(@Valid @RequestBody UserRequestDTO obj,
                                                    BindingResult binding,
                                                    @RequestParam(required = false) String uid) {
        return UtilsController.save(binding, () -> documentService.create(obj, uid));
    }

    @Operation(summary = "Register new user by role", description = "Registers a new user along with role-specific data (Resident, Operator, Recycler)")
    @PostMapping("/register")
    public ResponseEntity<ResponseStatusDTO> create(@Valid @RequestBody UserRegisterRequestDTO obj,
                                                    BindingResult binding,
                                                    @RequestParam(required = false) String uid) {
        return UtilsController.save(binding, ()->{
            var user=new UserRequestDTO(obj.getName(), obj.getLastName(), obj.getDni(), obj.getEmail(), obj.getPhone(), obj.getAddress(), obj.getImageUrl(), obj.getRole(), EStatus.ACTIVE);
            String userId = documentService.create(user, uid);

            GenericRequest<?> request = switch (obj.getRole()) {
                case RESIDENT -> new ResidentRequestDTO(0, true, userId);
                case RECYCLER, OPERATOR -> new WorkerRequestDTO(true, 0.0, new Date(), userId);
                default -> null;
            };

            return request!=null? documentService.create(request, userId) : userId;
        });
    }

    @Operation(summary = "List users", description = "Returns a paginated list of users")
    @GetMapping("/list")
    public CompletableFuture<ResponseEntity<?>> list(@RequestParam(defaultValue = "10", required = false) Integer limit) throws ExecutionException, InterruptedException {
        return UtilsController.list(documentService, new DefaultResponse(limit, className), className);
    }

    @Operation(summary = "Search users", description = "Performs a text-based search (e.g. name, email, etc.)")
    @GetMapping("/search")
    public CompletableFuture<ResponseEntity<?>> search(@RequestParam String search)  {
        if (search == null || search.trim().isEmpty()) {
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        }
        return UtilsController.search(documentService, new DefaultResponse(null, className), search, className);
    }

    @Operation(summary = "Get user by UID", description = "Fetches a user using their unique identifier (UID)")
    @GetMapping("/byId/{uid}")
    public ResponseEntity<?> byId(@PathVariable String uid) {
        return UtilsController.byId(documentService, DefaultResponse.defaultByID(uid, className));
    }

    @Operation(summary = "Get unique values for field", description = "Returns all unique values for a given field in the user collection attributes")
    @GetMapping("/setValuesPerField/{field}")
    public CompletableFuture<ResponseEntity<?>> setValuesPerField(@PathVariable String field) {
        var defaultResponse = DefaultResponse.defaultByID(null, className);
        return UtilsController.setValuesPerField(documentService, defaultResponse, field);
    }

    @Operation(summary = "Update user", description = "Updates general user attributes")
    @PutMapping("/update")
    public ResponseEntity<ResponseStatusDTO> update(@Valid @RequestBody UserRequestDTO obj,
                                                    BindingResult binding,
                                                    @RequestParam String uid) {
        var responseSupplier = DefaultResponse.defaultByID(uid, className);
        return UtilsController.save(binding, () -> documentService.update(responseSupplier, obj));
    }

    @Operation(summary = "Update user role", description = "Updates only the role of an existing user")
    @PutMapping("/update-role")
    public ResponseEntity<ResponseStatusDTO> update(@Valid @RequestBody ERole role,
                                                    BindingResult binding,
                                                    @RequestParam String uid) {
        var responseSupplier = DefaultResponse.defaultByID(uid, className);
        var obj=new UserRequestDTO(null, null,null, null, null, null, null, role, null);
        return UtilsController.save(binding, () -> documentService.update(responseSupplier, obj));
    }

    @Operation(summary = "Delete user", description = "Deletes a user based on their UID")
    @DeleteMapping("/delete/{uid}")
    public ResponseEntity<ResponseStatusDTO> delete(@PathVariable String uid) {
        return UtilsController.delete(() -> documentService.delete(DefaultResponse.defaultByID(uid, className)));
    }
}
