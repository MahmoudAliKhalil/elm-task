package com.github.mahmoudalikhalil.elm.controller;

import com.github.mahmoudalikhalil.elm.service.UserService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping(value = "/{id}/change-status")
    public ResponseEntity<Void> changeUserStatus(@Validated @PathVariable @Positive Long id, Principal principal) {
        userService.changeUserStatus(id, principal);
        return ResponseEntity.ok().build();
    }
}
