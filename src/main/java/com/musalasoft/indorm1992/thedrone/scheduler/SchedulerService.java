package com.musalasoft.indorm1992.thedrone.scheduler;

import com.musalasoft.indorm1992.thedrone.entity.Drone;
import com.musalasoft.indorm1992.thedrone.repository.DroneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@EnableScheduling
@Configuration
@ConditionalOnProperty(name = "scheduler.battery-level-logging.enabled", matchIfMissing = true)
@RequiredArgsConstructor
public class SchedulerService {

    private final DroneRepository droneRepository;

    @Scheduled(fixedDelayString = "${scheduler.battery-level-logging.interval-seconds}", timeUnit = TimeUnit.SECONDS)
    @Transactional(readOnly = true)
    public void logBatteries() {
        List<Drone> drones = droneRepository.findAll();
        log.info("+++++ Drones Battery Levels +++++");

        for (Drone drone : drones) {
            log.info("Drone battery level: {}%, id:{}, serial number:{}", drone.getBatteryCapacity(),
                    drone.getId(), drone.getSerialNumber());
        }
    }
}
