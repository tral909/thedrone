package com.musalasoft.indorm1992.thedrone.service;

import com.musalasoft.indorm1992.thedrone.dto.BatteryLevelDto;
import com.musalasoft.indorm1992.thedrone.dto.DroneCreateDto;
import com.musalasoft.indorm1992.thedrone.dto.DroneOutDto;
import com.musalasoft.indorm1992.thedrone.entity.Drone;
import com.musalasoft.indorm1992.thedrone.entity.DroneState;
import com.musalasoft.indorm1992.thedrone.exception.DroneNotFoundException;
import com.musalasoft.indorm1992.thedrone.mapper.DroneMapper;
import com.musalasoft.indorm1992.thedrone.repository.DroneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DroneService {

    private static final EnumSet<DroneState> AVAILABLE_DRONE_STATES = EnumSet.of(DroneState.IDLE, DroneState.LOADING);

    private final DroneMapper droneMapper;
    private final DroneRepository droneRepository;

    public DroneOutDto registerDrone(DroneCreateDto dto) {
        Drone drone = droneMapper.map(dto);
        Drone savedDrone = droneRepository.save(drone);
        return droneMapper.map(savedDrone);
    }

    @Transactional(readOnly = true)
    public BatteryLevelDto getDroneBatteryLevelById(Long id) {
        Drone drone = droneRepository.findById(id).orElseThrow(
                () -> new DroneNotFoundException("Drone with id=" + id + " is not found"));
        return new BatteryLevelDto(drone.getBatteryCapacity());
    }

    @Transactional(readOnly = true)
    public List<DroneOutDto> getAllFleetOfDrones() {
        return droneMapper.mapDrones(droneRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<DroneOutDto> getAvailableForLoadingDrones() {
        return droneMapper.mapDrones(droneRepository.findByStateIn(AVAILABLE_DRONE_STATES));
    }
}
