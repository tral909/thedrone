package com.musalasoft.indorm1992.thedrone.util;

import com.musalasoft.indorm1992.thedrone.dto.RestError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public RestError handleIllegalArgumentException(IllegalArgumentException ex) {
        return new RestError("this exception was handled by restcontroller advice");
    }

}