package com.musalasoft.indorm1992.thedrone.repository;

import com.musalasoft.indorm1992.thedrone.entity.Drone;
import com.musalasoft.indorm1992.thedrone.entity.DroneState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.EnumSet;
import java.util.List;

public interface DroneRepository extends JpaRepository<Drone, Long> {

    @Query("SELECT d FROM Drone d WHERE d.state IN :states AND d.batteryCapacity >= :level")
    List<Drone> findAvailableForLoading(@Param("states") EnumSet<DroneState> states,
                                        @Param("level") Integer minBatteryCapacity);
}
