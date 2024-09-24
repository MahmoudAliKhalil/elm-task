package com.github.mahmoudalikhalil.elm.model.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ProductCreationRequest(@NotBlank String name, @NotNull @PositiveOrZero @Digits(integer = 10, fraction = 2) BigDecimal price) {
}
