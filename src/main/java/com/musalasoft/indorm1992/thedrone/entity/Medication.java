package com.musalasoft.indorm1992.thedrone.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "MS_MEDICATION")
public class Medication extends AbstractEntity {

    @Column(name = "NAME", nullable = false)
    private String name;

    @Lob
    @Column(name = "IMAGE", nullable = false)
    private byte[] image;

    @Column(name = "WEIGHT_GRAMS", nullable = false)
    private Integer weightGrams;

    @Column(name = "CODE", nullable = false)
    private String code;

}