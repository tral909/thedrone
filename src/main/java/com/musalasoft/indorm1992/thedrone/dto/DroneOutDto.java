package com.musalasoft.indorm1992.thedrone.dto;

import com.musalasoft.indorm1992.thedrone.entity.DroneModel;
import com.musalasoft.indorm1992.thedrone.entity.DroneState;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DroneOutDto {

    Long id;
    String serialNumber;
    DroneModel model;
    Integer weightLimitGrams;
    Integer batteryCapacity;
    DroneState state;
}