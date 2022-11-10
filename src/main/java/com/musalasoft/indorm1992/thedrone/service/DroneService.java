package com.musalasoft.indorm1992.thedrone.service;

import com.musalasoft.indorm1992.thedrone.dto.DroneDto;
import com.musalasoft.indorm1992.thedrone.entity.Drone;
import com.musalasoft.indorm1992.thedrone.mapper.DroneMapper;
import com.musalasoft.indorm1992.thedrone.repository.DroneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DroneService {

    private final DroneMapper droneMapper;
    private final DroneRepository droneRepository;

    public DroneDto registerDrone(DroneDto dto) {
        Drone drone = droneMapper.map(dto);
        Drone savedDrone = droneRepository.save(drone);
        return droneMapper.map(savedDrone);
    }

}
