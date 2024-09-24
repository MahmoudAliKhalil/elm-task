package com.github.mahmoudalikhalil.elm.controller;

import com.github.mahmoudalikhalil.elm.exception.UserNotFoundException;
import com.github.mahmoudalikhalil.elm.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.Principal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private final Principal principal = () -> "1";

    @Test
    void shouldReturnSuccessfulResponseWhenChangeUserStatusSucceed() {
        Long userId = 1L;

        ResponseEntity<Void> response = userController.changeUserStatus(userId, principal);

        assertThat(response).isNotNull()
                .extracting(ResponseEntity::getStatusCode)
                .isSameAs(HttpStatus.OK);

        verify(userService).changeUserStatus(userId, principal);
    }

    @Test
    void shouldThrowsExceptionWhenChangeUserStatusFails() {
        Long userId = 1L;

        doThrow(new UserNotFoundException()).when(userService).changeUserStatus(userId, principal);

        assertThatThrownBy(() -> userController.changeUserStatus(userId, principal))
                .isExactlyInstanceOf(UserNotFoundException.class);

        verify(userService).changeUserStatus(userId, principal);
    }
}