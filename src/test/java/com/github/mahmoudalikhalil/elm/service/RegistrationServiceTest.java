package com.github.mahmoudalikhalil.elm.service;

import com.github.mahmoudalikhalil.elm.mapper.UserMapper;
import com.github.mahmoudalikhalil.elm.model.dto.ClientUserRegistrationRequest;
import com.github.mahmoudalikhalil.elm.model.dto.SuperUserRegistrationRequest;
import com.github.mahmoudalikhalil.elm.model.dto.SuperUserRole;
import com.github.mahmoudalikhalil.elm.model.entity.User;
import com.github.mahmoudalikhalil.elm.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private RegistrationService registrationService;

    @Test
    void shouldRegisterClientSuccessfully() {
        ClientUserRegistrationRequest request = new ClientUserRegistrationRequest("test", "test@example.com", "dummyPassword");
        String encodedPassword = "encodedPassword";
        User user = new User();
        when(passwordEncoder.encode(request.password())).thenReturn(encodedPassword);
        when(userMapper.mapClientRegistrationRequestToUser(request, encodedPassword)).thenReturn(user);

        assertThatCode(() -> registrationService.registerClient(request))
                .doesNotThrowAnyException();

        verify(passwordEncoder).encode(request.password());
        verify(userMapper).mapClientRegistrationRequestToUser(request, encodedPassword);
        verify(userRepository).save(user);
    }

    @EnumSource(SuperUserRole.class)
    @ParameterizedTest
    void shouldRegisterSuperUserSuccessfully(SuperUserRole role) {
        SuperUserRegistrationRequest request = new SuperUserRegistrationRequest("test", "test@example.com", "dummyPassword", role);
        String encodedPassword = "encodedPassword";
        User user = new User();
        when(passwordEncoder.encode(request.password())).thenReturn(encodedPassword);
        when(userMapper.mapSuperUserRegistrationRequestToUser(request, encodedPassword)).thenReturn(user);

        assertThatCode(() -> registrationService.registerAdminOrDealer(request))
                .doesNotThrowAnyException();

        verify(passwordEncoder).encode(request.password());
        verify(userMapper).mapSuperUserRegistrationRequestToUser(request, encodedPassword);
        verify(userRepository).save(user);
    }
}