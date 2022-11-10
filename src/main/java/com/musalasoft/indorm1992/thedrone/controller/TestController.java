package com.musalasoft.indorm1992.thedrone.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Operation(summary = "Get a book by its id")
    @GetMapping(value = "api/v1/test")
    public String test() {

        //throw new IllegalArgumentException("oh no, illegal argument was passed");
        return "test success";
    }

}