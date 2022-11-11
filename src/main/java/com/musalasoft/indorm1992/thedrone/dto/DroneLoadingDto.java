package com.musalasoft.indorm1992.thedrone.dto;

import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Value
public class DroneLoadingDto {
    @NotEmpty
    @Valid
    List<MedicationCreateDto> medications = new ArrayList<>();
}
