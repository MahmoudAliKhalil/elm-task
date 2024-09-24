package com.github.mahmoudalikhalil.elm.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(@NotBlank String username, @NotBlank @Size(min = 8) String password) {
}
