package org.system.monitoring.infrastructure.adapter;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.system.monitoring.domain.collection.dto.request.UserRequestDTO;
import org.system.monitoring.domain.collection.dto.response.UserResponseDTO;
import org.system.monitoring.infrastructure.firebase.DefaultResponse;
import org.system.monitoring.infrastructure.firebase.service.DocumentService;
import org.system.monitoring.infrastructure.firebase.util.ERole;
import org.system.monitoring.infrastructure.firebase.util.ResponseStatusDTO;
import org.system.monitoring.infrastructure.firebase.util.UtilsController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    private final Class<UserResponseDTO> className = UserResponseDTO.class;

    @Autowired
    private DocumentService principalService;

    @PostMapping("/create")
    public ResponseEntity<ResponseStatusDTO> create(@Valid @RequestBody UserRequestDTO obj,
                                                    BindingResult binding,
                                                    @RequestParam(required = false) String uid) {
        return UtilsController.save(binding, () -> principalService.create(obj, uid));
    }


    @GetMapping("/list")
    public CompletableFuture<ResponseEntity<?>> list(@RequestParam(defaultValue = "10", required = false) Integer limit) throws ExecutionException, InterruptedException {
        return UtilsController.list(principalService, new DefaultResponse(limit, className), className);
    }

    @GetMapping("/search")
    public CompletableFuture<ResponseEntity<?>> search(@RequestParam String search)  {
        if (search == null || search.trim().isEmpty()) {
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        }
        return UtilsController.search(principalService, new DefaultResponse(null, className), search, className);
    }

    @GetMapping("/byId/{uid}")
    public ResponseEntity<?> byId(@PathVariable String uid) {
        return UtilsController.byId(principalService, DefaultResponse.defaultByID(uid, className));
    }

    @GetMapping("/setValuesPerField/{field}")
    public CompletableFuture<ResponseEntity<?>> setValuesPerField(@PathVariable String field) {
        var defaultResponse = DefaultResponse.defaultByID(null, className);
        return UtilsController.setValuesPerField(principalService, defaultResponse, field);
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseStatusDTO> update(@Valid @RequestBody UserRequestDTO obj,
                                                    BindingResult binding,
                                                    @RequestParam(required = false) String uid) {
        var responseSupplier = DefaultResponse.defaultByID(uid, className);
        return UtilsController.save(binding, () -> principalService.update(responseSupplier, obj));
    }

    @PutMapping("/update-role")
    public ResponseEntity<ResponseStatusDTO> update(@Valid @RequestBody ERole role,
                                                    BindingResult binding,
                                                    @RequestParam(required = false) String uid) {
        var responseSupplier = DefaultResponse.defaultByID(uid, className);
        var obj=new UserRequestDTO(null, null,null, null, null, null, null, role);
        return UtilsController.save(binding, () -> principalService.update(responseSupplier, obj));
    }

    @DeleteMapping("/delete/{uid}")
    public ResponseEntity<ResponseStatusDTO> delete(@PathVariable String uid) {
        return UtilsController.delete(() -> principalService.delete(DefaultResponse.defaultByID(uid, className)));
    }
}
