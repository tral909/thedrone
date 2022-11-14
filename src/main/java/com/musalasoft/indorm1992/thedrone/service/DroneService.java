package com.musalasoft.indorm1992.thedrone.service;

import com.musalasoft.indorm1992.thedrone.dto.BatteryLevelDto;
import com.musalasoft.indorm1992.thedrone.dto.DroneCreateDto;
import com.musalasoft.indorm1992.thedrone.dto.DroneLoadingDto;
import com.musalasoft.indorm1992.thedrone.dto.DroneOutDto;
import com.musalasoft.indorm1992.thedrone.dto.MedicationDto;
import com.musalasoft.indorm1992.thedrone.entity.Drone;
import com.musalasoft.indorm1992.thedrone.entity.DroneState;
import com.musalasoft.indorm1992.thedrone.entity.Medication;
import com.musalasoft.indorm1992.thedrone.exception.DroneLoadingException;
import com.musalasoft.indorm1992.thedrone.exception.DroneNotFoundException;
import com.musalasoft.indorm1992.thedrone.exception.DroneOverloadedException;
import com.musalasoft.indorm1992.thedrone.exception.DroneUniqueSerialNumberException;
import com.musalasoft.indorm1992.thedrone.mapper.DroneMapper;
import com.musalasoft.indorm1992.thedrone.mapper.MedicationMapper;
import com.musalasoft.indorm1992.thedrone.repository.DroneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumSet;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DroneService {

    private static final EnumSet<DroneState> AVAILABLE_FOR_LOADING_DRONE_STATES =
            EnumSet.of(DroneState.IDLE, DroneState.LOADING);
    private static final Integer DRONE_BATTERY_MIN_FOR_LOADING = 25;

    private final DroneMapper droneMapper;
    private final MedicationMapper medicationMapper;
    private final DroneRepository droneRepository;

    @Transactional
    public DroneOutDto registerDrone(DroneCreateDto dto) {
        log.info("registerDrone starts with request: {}", dto);
        droneRepository.findBySerialNumber(dto.getSerialNumber())
                .ifPresent((d) -> {
                    throw new DroneUniqueSerialNumberException(
                            "Drone with serial number " + dto.getSerialNumber() + " already exists");
                });
        Drone drone = droneMapper.map(dto);
        Drone savedDrone = droneRepository.save(drone);
        DroneOutDto outDto = droneMapper.map(savedDrone);
        log.info("A drone finishes registering successfully. Response: {}", outDto);
        return outDto;
    }

    @Transactional(readOnly = true)
    public BatteryLevelDto getDroneBatteryLevelById(Long id) {
        log.info("getDroneBatteryLevelById starts for id={}", id);
        Drone drone = droneRepository.findById(id).orElseThrow(
                () -> new DroneNotFoundException("Drone with id=" + id + " is not found"));
        log.info("getDroneBatteryLevelById finishes with result {} for drone id={}",
                drone.getBatteryCapacity(), id);
        return new BatteryLevelDto(drone.getBatteryCapacity());
    }

    @Transactional(readOnly = true)
    public List<DroneOutDto> getAllFleetOfDrones() {
        log.info("getAllFleetOfDrones starts");
        List<DroneOutDto> response = droneMapper.mapDrones(droneRepository.findAll());
        log.info("getAllFleetOfDrones finishes");
        return response;
    }

    @Transactional(readOnly = true)
    public List<DroneOutDto> getAvailableForLoadingDrones() {
        log.info("getAvailableForLoadingDrones starts");
        List<DroneOutDto> response = droneMapper.mapDrones(droneRepository.findAvailableForLoading(
                AVAILABLE_FOR_LOADING_DRONE_STATES,
                DRONE_BATTERY_MIN_FOR_LOADING));
        log.info("getAvailableForLoadingDrones finishes");
        return response;
    }

    @Transactional
    public void loadDroneWithMedications(Long id, DroneLoadingDto dto) {
        log.info("loadDroneWithMedications starts for id={} with request: {}", id, dto);
        Drone drone = droneRepository.findById(id).orElseThrow(
                () -> new DroneNotFoundException("Drone with id=" + id + " is not found"));

        if (!AVAILABLE_FOR_LOADING_DRONE_STATES.contains(drone.getState())
                || drone.getBatteryCapacity() < DRONE_BATTERY_MIN_FOR_LOADING) {
            throw new DroneLoadingException("Drone with id=" + id + " is not available for loading");
        }

        Integer totalWeightForLoad = dto.getMedications().stream()
                .map(MedicationDto::getWeightGrams)
                .reduce(0, Integer::sum);

        Integer alreadyLoadedWeight = drone.getMedications().stream()
                .map(Medication::getWeightGrams)
                .reduce(0, Integer::sum);

        if (totalWeightForLoad + alreadyLoadedWeight <= drone.getWeightLimitGrams()) {
            drone.getMedications().addAll(medicationMapper.mapDtos(dto.getMedications()));
            drone.setState(DroneState.LOADING);
        } else {
            throw new DroneOverloadedException("Too much medications! Loading is rejected...");
        }

        if (totalWeightForLoad + alreadyLoadedWeight == drone.getWeightLimitGrams()) {
            drone.setState(DroneState.LOADED);
        }
        log.info("loadDroneWithMedications finishes for id={} with request: {}", id, dto);
    }

    @Transactional(readOnly = true)
    public List<MedicationDto> getLoadedMedicationByDroneId(Long id) {
        log.info("getLoadedMedicationByDroneId starts with id={}", id);
        Drone drone = droneRepository.findById(id).orElseThrow(
                () -> new DroneNotFoundException("Drone with id=" + id + " is not found"));

        List<MedicationDto> response = medicationMapper.mapMeds(drone.getMedications());
        log.info("getLoadedMedicationByDroneId finishes for id={}", id);
        return response;
    }
}
