package com.musalasoft.indorm1992.thedrone.dto;

import lombok.Value;

import java.util.List;

@Value
public class RestError {
    String reason;
    List<String> errors;
}
