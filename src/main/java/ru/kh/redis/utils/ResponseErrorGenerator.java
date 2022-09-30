package ru.kh.redis.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class ResponseErrorGenerator {

    public static ResponseEntity<String> generateErrorResponseByBinding(BindingResult bindingResult) {
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

    public static ResponseEntity<String> generateErrorResponseWithMessage(String message) {
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<String> validateResultForKeyNotFoundOrWrongTypeErrors(String result) {
        if (result.equals(Consts.ERROR_MESSAGE_KEY_NOT_FOUND)) {
            return ResponseErrorGenerator.generateErrorResponseWithMessage(Consts.ERROR_MESSAGE_KEY_NOT_FOUND);
        }
        if (result.equals(Consts.ERROR_MESSAGE_WRONG_TYPE)) {
            return ResponseErrorGenerator.generateErrorResponseWithMessage(Consts.ERROR_MESSAGE_WRONG_TYPE);
        }
        return null;
    }
}
