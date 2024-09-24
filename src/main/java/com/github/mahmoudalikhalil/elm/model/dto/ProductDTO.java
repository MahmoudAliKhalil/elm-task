package com.github.mahmoudalikhalil.elm.model.dto;

import com.github.mahmoudalikhalil.elm.model.entity.Status;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductDTO(Long id, String dealerName, String name, BigDecimal price, Status status) {
}
