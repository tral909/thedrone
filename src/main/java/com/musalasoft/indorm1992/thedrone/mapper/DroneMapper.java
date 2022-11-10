package com.musalasoft.indorm1992.thedrone.mapper;

import com.musalasoft.indorm1992.thedrone.dto.DroneDto;
import com.musalasoft.indorm1992.thedrone.entity.Drone;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DroneMapper {

    Drone map(DroneDto dto);
    DroneDto map(Drone drone);
}
