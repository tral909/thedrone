package com.musalasoft.indorm1992.thedrone.mapper;

import com.musalasoft.indorm1992.thedrone.dto.MedicationCreateDto;
import com.musalasoft.indorm1992.thedrone.dto.MedicationOutDto;
import com.musalasoft.indorm1992.thedrone.entity.Medication;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MedicationMapper {

    Medication map(MedicationCreateDto dto);
    MedicationOutDto map(Medication medication);

    List<Medication> mapMedsToSave(List<MedicationCreateDto> medications);
}