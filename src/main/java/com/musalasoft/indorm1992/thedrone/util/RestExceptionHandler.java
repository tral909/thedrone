package com.musalasoft.indorm1992.thedrone.util;

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
    public RestError MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex) {

        List<String> errors = new ArrayList<>();
        for (FieldError err : ex.getFieldErrors()) {
            errors.add(err.getField() + ": " + err.getDefaultMessage());
        }

        return new RestError(HttpStatus.BAD_REQUEST.getReasonPhrase(), errors);
    }

}