package com.musalasoft.indorm1992.thedrone.controller;

import com.musalasoft.indorm1992.thedrone.dto.DroneDto;
import com.musalasoft.indorm1992.thedrone.service.DroneService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/drone")
public class DroneController {

    private final DroneService droneService;

    @Operation(summary = "Register new drone")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DroneDto registerDrone(@RequestBody @Valid DroneDto dto) {
        return droneService.registerDrone(dto);
    }

}