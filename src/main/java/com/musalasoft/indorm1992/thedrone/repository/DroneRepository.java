package com.musalasoft.indorm1992.thedrone.repository;

import com.musalasoft.indorm1992.thedrone.entity.Drone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DroneRepository extends JpaRepository<Drone, Long> {
}
