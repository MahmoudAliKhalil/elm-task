package com.github.mahmoudalikhalil.elm.mapper;

import com.github.mahmoudalikhalil.elm.model.dto.ClientUserRegistrationRequest;
import com.github.mahmoudalikhalil.elm.model.dto.SuperUserRegistrationRequest;
import com.github.mahmoudalikhalil.elm.model.dto.SuperUserRole;
import com.github.mahmoudalikhalil.elm.model.entity.Status;
import com.github.mahmoudalikhalil.elm.model.entity.User;
import com.github.mahmoudalikhalil.elm.model.entity.UserRole;
import com.github.mahmoudalikhalil.elm.model.entity.User_;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;

import java.security.Principal;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserMapperTest {
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    static Stream<Arguments> validUserRoles() {
        return Stream.of(
                Arguments.of(SuperUserRole.ADMIN, UserRole.ADMIN),
                Arguments.of(SuperUserRole.DEALER, UserRole.DEALER)
        );
    }

    @Test
    void shouldMapClientRegistrationRequestToUserEntity() {
        String username = "username";
        String email = "test@example.com";
        String encodedPassword = "encodedPassword";
        ClientUserRegistrationRequest request = new ClientUserRegistrationRequest(username, "password", email);

        User user = userMapper.mapClientRegistrationRequestToUser(request, encodedPassword);

        assertThat(user).isNotNull()
                .hasFieldOrPropertyWithValue(User_.USERNAME, username)
                .hasFieldOrPropertyWithValue(User_.PASSWORD, encodedPassword)
                .hasFieldOrPropertyWithValue(User_.ROLE, UserRole.CLIENT)
                .hasFieldOrPropertyWithValue(User_.STATUS, Status.ACTIVE);
    }

    @MethodSource("validUserRoles")
    @ParameterizedTest
    void shouldMapSuperUserRegistrationRequestToUserEntity(SuperUserRole role, UserRole exceptedRole) {
        String username = "username";
        String email = "test@example.com";
        String encodedPassword = "encodedPassword";
        SuperUserRegistrationRequest request = new SuperUserRegistrationRequest(username, "password", email, role);

        User user = userMapper.mapSuperUserRegistrationRequestToUser(request, encodedPassword);

        assertThat(user).isNotNull()
                .hasFieldOrPropertyWithValue(User_.USERNAME, username)
                .hasFieldOrPropertyWithValue(User_.PASSWORD, encodedPassword)
                .hasFieldOrPropertyWithValue(User_.ROLE, exceptedRole)
                .hasFieldOrPropertyWithValue(User_.STATUS, Status.ACTIVE);
    }

    @Test
    void shouldMapPrincipalToUserEntity() {
        Principal principal = when(mock(Principal.class).getName())
                .thenReturn("1")
                .getMock();

        User user = userMapper.mapPrinicepalToUser(principal);

        assertThat(user).isNotNull()
                .hasAllNullFieldsOrPropertiesExcept(User_.ID)
                .hasFieldOrPropertyWithValue(User_.ID, 1L);
    }
}