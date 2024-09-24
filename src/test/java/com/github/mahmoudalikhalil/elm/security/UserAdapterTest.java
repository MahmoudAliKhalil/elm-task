package com.github.mahmoudalikhalil.elm.security;

import com.github.mahmoudalikhalil.elm.model.entity.Status;
import com.github.mahmoudalikhalil.elm.model.entity.User;
import com.github.mahmoudalikhalil.elm.model.entity.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

class UserAdapterTest {
    private User user;
    private UserAdapter userAdapter;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .username("username")
                .password("password")
                .role(UserRole.CLIENT)
                .status(Status.ACTIVE)
                .build();

        userAdapter = new UserAdapter(user);
    }

    @Test
    void shouldGetUsernameReturnUsername() {
        String username = userAdapter.getUsername();

        assertThat(username).isEqualTo("username");
    }

    @Test
    void shouldGetPasswordReturnPassword() {
        String password = userAdapter.getPassword();

        assertThat(password).isEqualTo("password");
    }

    @Test
    void shouldGetAuthoritiesReturnUserRole() {
        Collection<? extends GrantedAuthority> authorities = userAdapter.getAuthorities();

        assertThat(authorities).singleElement()
                .isEqualTo(user.getRole());
    }

    @Test
    void shouldIsEnabledReturnTrueWhenUserIsActive() {
        boolean isEnabled = userAdapter.isEnabled();

        assertThat(isEnabled).isTrue();
    }

    @Test
    void shouldIsEnabledReturnFalseWhenUserIsInactive() {
        userAdapter.user().setStatus(Status.INACTIVE);

        boolean isEnabled = userAdapter.isEnabled();

        assertThat(isEnabled).isFalse();
    }
}