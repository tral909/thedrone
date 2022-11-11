package com.musalasoft.indorm1992.thedrone.exception;

import com.musalasoft.indorm1992.thedrone.dto.RestError;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class RestExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public RestError handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        List<String> errors = new ArrayList<>();
        for (FieldError err : ex.getFieldErrors()) {
            errors.add(err.getField() + ": " + err.getDefaultMessage());
        }

        return new RestError(HttpStatus.BAD_REQUEST.getReasonPhrase(), errors);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ DroneNotFoundException.class, DroneOverloadedException.class, DroneLoadingException.class })
    public RestError handleInnerValidationExceptions(RuntimeException ex) {
        return new RestError(HttpStatus.BAD_REQUEST.getReasonPhrase(), List.of(ex.getMessage()));
    }

}