package com.github.mahmoudalikhalil.elm.controller;

import com.github.mahmoudalikhalil.elm.model.dto.ClientUserRegistrationRequest;
import com.github.mahmoudalikhalil.elm.model.dto.SuperUserRegistrationRequest;
import com.github.mahmoudalikhalil.elm.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> registerClient(@RequestBody @Valid ClientUserRegistrationRequest request) {
        registrationService.registerClient(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/users/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> registerAdminOrDealer(@RequestBody @Valid SuperUserRegistrationRequest request) {
        registrationService.registerAdminOrDealer(request);
        return ResponseEntity.ok().build();
    }
}
