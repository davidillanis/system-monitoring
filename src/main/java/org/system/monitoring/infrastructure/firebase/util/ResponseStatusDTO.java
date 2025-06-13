package org.system.monitoring.infrastructure.firebase.util;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.stream.Collectors;

@JsonPropertyOrder({"isSuccess", "message", "errors"})
public record ResponseStatusDTO(Boolean isSuccess, EMessage message, List<String> errors, String data) {

    public static ResponseStatusDTO responseBinding(BindingResult bindingResult, EMessage message) {
        List<String> errors = bindingResult.getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());
        return new ResponseStatusDTO(false, message, errors, null);
    }

    public static ResponseStatusDTO getInstance(Boolean isSuccess, EMessage message, List<String> errors){
        return new ResponseStatusDTO(isSuccess, message, errors, null);
    }

    public static ResponseStatusDTO getInstance(Boolean isSuccess, EMessage message, List<String> errors, String data){
        return new ResponseStatusDTO(isSuccess, message, errors, data);
    }
}