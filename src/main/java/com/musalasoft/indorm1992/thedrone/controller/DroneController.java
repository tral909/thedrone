package com.musalasoft.indorm1992.thedrone.controller;

import com.musalasoft.indorm1992.thedrone.dto.BatteryLevelDto;
import com.musalasoft.indorm1992.thedrone.dto.DroneCreateDto;
import com.musalasoft.indorm1992.thedrone.dto.DroneOutDto;
import com.musalasoft.indorm1992.thedrone.service.DroneService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/drone")
public class DroneController {

    private final DroneService droneService;

    @Operation(summary = "Register new drone")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DroneOutDto registerDrone(@RequestBody @Valid DroneCreateDto dto) {
        return droneService.registerDrone(dto);
    }

    @Operation(summary = "Check drone battery level")
    @GetMapping("{id}/battery-level")
    public BatteryLevelDto getDroneBatteryLevelById(@PathVariable Long id) {
        return droneService.getDroneBatteryLevelById(id);
    }

    @Operation(summary = "Get available drones for loading")
    @GetMapping("available-for-loading")
    public List<DroneOutDto> getAvailableForLoadingDrones() {
        return droneService.getAvailableForLoadingDrones();
    }

    @Operation(summary = "Get all fleet of drones")
    @GetMapping
    public List<DroneOutDto> getAllFleetOfDrones() {
        return droneService.getAllFleetOfDrones();
    }
}