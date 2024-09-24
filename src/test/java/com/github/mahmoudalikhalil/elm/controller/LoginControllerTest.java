package com.github.mahmoudalikhalil.elm.controller;

import com.github.mahmoudalikhalil.elm.exception.UserNotFoundException;
import com.github.mahmoudalikhalil.elm.model.dto.LoginRequest;
import com.github.mahmoudalikhalil.elm.model.dto.LoginResponse;
import com.github.mahmoudalikhalil.elm.service.LoginService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {
    @Mock
    private LoginService loginService;

    @InjectMocks
    private LoginController loginController;

    @Test
    void shouldReturnSuccessfulResponseWhenLoginSucceed() {
        LoginRequest request = new LoginRequest("username", "password");
        LoginResponse expectedResponse = new LoginResponse("token");

        when(loginService.login(request)).thenReturn(expectedResponse);

        ResponseEntity<LoginResponse> response = loginController.login(request);

        assertThat(response).isNotNull()
                .satisfies(responseEntity -> {
                    assertThat(responseEntity.getStatusCode())
                            .isSameAs(HttpStatus.OK);

                    assertThat(responseEntity.getBody())
                            .isNotNull()
                            .isEqualTo(expectedResponse);
                });

        verify(loginService).login(request);
    }

    @Test
    void shouldThrowsExceptionWhenLoginFails() {
        LoginRequest request = new LoginRequest("wrongUsername", "wrongPassword");

        when(loginService.login(request)).thenThrow(new UserNotFoundException());

        assertThatThrownBy(() -> loginController.login(request))
                .isExactlyInstanceOf(UserNotFoundException.class);

        verify(loginService).login(request);
    }
}