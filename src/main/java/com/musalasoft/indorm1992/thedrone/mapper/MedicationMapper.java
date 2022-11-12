package com.musalasoft.indorm1992.thedrone.mapper;

import com.musalasoft.indorm1992.thedrone.dto.MedicationDto;
import com.musalasoft.indorm1992.thedrone.entity.Medication;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MedicationMapper {

    Medication map(MedicationDto dto);
    MedicationDto map(Medication medication);

    List<Medication> mapDtos(List<MedicationDto> dtos);
    List<MedicationDto> mapMeds(List<Medication> meds);
}