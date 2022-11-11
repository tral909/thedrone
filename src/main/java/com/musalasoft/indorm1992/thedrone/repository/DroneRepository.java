package com.musalasoft.indorm1992.thedrone.repository;

import com.musalasoft.indorm1992.thedrone.entity.Drone;
import com.musalasoft.indorm1992.thedrone.entity.DroneState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.EnumSet;
import java.util.List;

public interface DroneRepository extends JpaRepository<Drone, Long> {

    List<Drone> findByStateIn(EnumSet<DroneState> states);
}
