package com.musalasoft.indorm1992.thedrone.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "MS_DRONE")
public class Drone extends AbstractEntity {

    @Column(name = "SERIAL_NUMBER", unique = true, nullable = false)
    private String serialNumber;

    @Column(name = "MODEL", nullable = false)
    @Enumerated(EnumType.STRING)
    private DroneModel model;

    @Column(name = "WEIGHT_LIMIT_GRAMS", nullable = false)
    private Integer weightLimitGrams;

    @Column(name = "BATTERY_CAPACITY", nullable = false)
    private Integer batteryCapacity;

    @Column(name = "STATE", nullable = false)
    @Enumerated(EnumType.STRING)
    private DroneState state = DroneState.IDLE;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "DRONE_ID")
    private List<Medication> medications = new ArrayList<>();

}