package com.musalasoft.indorm1992.thedrone.dto;

import lombok.Value;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Value
public class MedicationCreateDto {

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$")
    String name;

    @NotEmpty
    byte[] image;

    @NotNull
    @Min(0)
    @Max(500)
    Integer weightGrams;

    @NotBlank
    @Pattern(regexp = "^[A-Z0-9_]+$")
    String code;
}
