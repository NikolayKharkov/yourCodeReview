package ru.kh.redis.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class ResponseErrorGenerator {

    public static ResponseEntity<String> generateErrorResponse(BindingResult bindingResult) {
        StringBuilder errorMessage = new StringBuilder();
        for (FieldError error : bindingResult.getFieldErrors()) {
            errorMessage
                    .append(error.getField())
                    .append("-")
                    .append(error.getDefaultMessage())
                    .append(";");
        }
        return new ResponseEntity<>(errorMessage.toString(), HttpStatus.BAD_REQUEST);
    }
}
