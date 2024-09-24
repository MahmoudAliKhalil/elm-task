package com.github.mahmoudalikhalil.elm.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ClientUserRegistrationRequest(@NotBlank String username, @NotBlank @Size(min = 8) String password, @NotNull @Email String email) {
}
