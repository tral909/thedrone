package com.musalasoft.indorm1992.thedrone.dto;

import com.musalasoft.indorm1992.thedrone.entity.DroneModel;
import lombok.Value;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
public class DroneCreateDto {

    Long id;

    @NotBlank
    @Size(min = 3, max = 100)
    String serialNumber;

    @NotNull
    DroneModel model;

    @NotNull
    @Min(0)
    @Max(500)
    Integer weightLimitGrams;

    @NotNull
    @Min(0)
    @Max(100)
    Integer batteryCapacity;

}
