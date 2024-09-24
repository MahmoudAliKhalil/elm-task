package com.github.mahmoudalikhalil.elm.service;

import com.github.mahmoudalikhalil.elm.model.dto.LoginRequest;
import com.github.mahmoudalikhalil.elm.model.dto.LoginResponse;
import com.github.mahmoudalikhalil.elm.model.entity.User;
import com.github.mahmoudalikhalil.elm.security.UserAdapter;
import com.github.mahmoudalikhalil.elm.util.JWTUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTUtil jwtUtil;

    @InjectMocks
    private LoginService loginService;

    @Test
    void shouldReturnTokenWhenTheUserIsAuthenticated() {
        String username = "test";
        String password = "dummyPassword";
        User user = new User();
        String token = UUID.randomUUID().toString();

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(new UserAdapter(user));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(jwtUtil.generateJWT(user)).thenReturn(token);

        LoginResponse response = loginService.login(new LoginRequest(username, password));

        assertThat(response).isNotNull()
                .extracting(LoginResponse::token)
                .isEqualTo(token);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtil).generateJWT(user);
    }

    @Test
    void shouldThrowAuthenticationExceptionWhenAuthenticationDoesNotReturnUserAdapterPrincipal() {
        LoginRequest loginRequest = new LoginRequest("test", "dummyPassword");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(new Object());
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        assertThatThrownBy(() -> loginService.login(loginRequest))
                .isInstanceOf(AuthenticationServiceException.class)
                .hasMessageStartingWith("Expecting principal of type UserAdapter but found");

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}