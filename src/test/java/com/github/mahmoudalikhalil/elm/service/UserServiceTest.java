package com.github.mahmoudalikhalil.elm.service;

import com.github.mahmoudalikhalil.elm.exception.AdminSelfUpdateException;
import com.github.mahmoudalikhalil.elm.exception.UserNotFoundException;
import com.github.mahmoudalikhalil.elm.model.entity.Status;
import com.github.mahmoudalikhalil.elm.model.entity.User;
import com.github.mahmoudalikhalil.elm.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Principal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private Principal principal;

    @BeforeEach
    void setUp() {
        principal = mock(Principal.class);
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenUserIsNotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.changeUserStatus(userId, principal))
                .isExactlyInstanceOf(UserNotFoundException.class);

        verify(userRepository).findById(userId);
    }

    @Test
    void shouldThrowAdminSelfUpdateExceptionWhenAdminTriesToUpdateHisStatus() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(principal.getName()).thenReturn(userId.toString());

        assertThatThrownBy(() -> userService.changeUserStatus(userId, principal))
                .isExactlyInstanceOf(AdminSelfUpdateException.class);

        verify(userRepository).findById(userId);
    }

    @Test
    void shouldChangeUserStatusSuccessfully() {
        Long userId = 2L;
        User user = new User();
        user.setStatus(Status.ACTIVE);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(principal.getName()).thenReturn("1");

        userService.changeUserStatus(userId, principal);

        assertThat(user).extracting(User::getStatus)
                        .isSameAs(Status.INACTIVE);

        verify(userRepository).findById(userId);
    }
}