package com.musalasoft.indorm1992.thedrone.mapper;

import com.musalasoft.indorm1992.thedrone.dto.DroneCreateDto;
import com.musalasoft.indorm1992.thedrone.dto.DroneOutDto;
import com.musalasoft.indorm1992.thedrone.entity.Drone;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DroneMapper {

    Drone map(DroneCreateDto dto);
    DroneOutDto map(Drone drone);

    List<DroneOutDto> mapDrones(List<Drone> drones);
}
