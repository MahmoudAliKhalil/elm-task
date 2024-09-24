package com.github.mahmoudalikhalil.elm.security;

import com.github.mahmoudalikhalil.elm.constant.Constant;
import com.github.mahmoudalikhalil.elm.model.entity.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JWTValidatorsTest {
    @Mock
    private Jwt jwt;

    @Test
    void shouldReturnSuccessResultWhenValidatingJWTTokenWithNotBeforeClaim() {
        Instant now = Instant.now();
        when(this.jwt.getClaim(JwtClaimNames.NBF)).thenReturn(now);

        OAuth2TokenValidatorResult result = JWTValidators.notBeforeValidator().validate(jwt);

        assertThat(result.hasErrors()).isFalse();

        verify(jwt).getClaim(JwtClaimNames.NBF);
    }

    @Test
    void shouldReturnFailedResultWhenValidatingJWTTokenWithoutNotBeforeClaim() {
        OAuth2TokenValidatorResult result = JWTValidators.notBeforeValidator().validate(jwt);

        assertThat(result.hasErrors()).isTrue();

        verify(jwt).getClaim(JwtClaimNames.NBF);
    }

    @Test
    void shouldReturnSuccessResultWhenValidatingJWTTokenWithIssuedAtClaim() {
        Instant now = Instant.now();
        when(this.jwt.getClaim(JwtClaimNames.IAT)).thenReturn(now);

        OAuth2TokenValidatorResult result = JWTValidators.issuedAtValidator().validate(jwt);

        assertThat(result.hasErrors()).isFalse();

        verify(jwt).getClaim(JwtClaimNames.IAT);
    }

    @Test
    void shouldReturnFailedResultWhenValidatingJWTTokenWithoutIssuedAtClaim() {
        OAuth2TokenValidatorResult result = JWTValidators.issuedAtValidator().validate(jwt);

        assertThat(result.hasErrors()).isTrue();

        verify(jwt).getClaim(JwtClaimNames.IAT);
    }

    @Test
    void shouldReturnSuccessResultWhenValidatingJWTTokenWithExpireAtClaim() {
        Instant now = Instant.now();
        when(this.jwt.getClaim(JwtClaimNames.EXP)).thenReturn(now);

        OAuth2TokenValidatorResult result = JWTValidators.expireAtValidator().validate(jwt);

        assertThat(result.hasErrors()).isFalse();

        verify(jwt).getClaim(JwtClaimNames.EXP);
    }

    @Test
    void shouldReturnFailedResultWhenValidatingJWTTokenWithoutExpireAtClaim() {
        OAuth2TokenValidatorResult result = JWTValidators.expireAtValidator().validate(jwt);

        assertThat(result.hasErrors()).isTrue();

        verify(jwt).getClaim(JwtClaimNames.EXP);
    }

    @Test
    void shouldReturnSuccessResultWhenValidatingJWTTokenWithSubjectClaim() {
        when(this.jwt.getClaim(JwtClaimNames.SUB)).thenReturn("1");

        OAuth2TokenValidatorResult result = JWTValidators.subjectValidator().validate(jwt);

        assertThat(result.hasErrors()).isFalse();

        verify(jwt).getClaim(JwtClaimNames.SUB);
    }

    @Test
    void shouldReturnFailedResultWhenValidatingJWTTokenWithoutSubjectClaim() {
        OAuth2TokenValidatorResult result = JWTValidators.subjectValidator().validate(jwt);

        assertThat(result.hasErrors()).isTrue();

        verify(jwt).getClaim(JwtClaimNames.SUB);
    }

    @Test
    void shouldReturnSuccessResultWhenValidatingJWTTokenWithRoleClaim() {
        when(this.jwt.getClaim(Constant.ROLE_CLAIM_NAME)).thenReturn(UserRole.ADMIN.getAuthority());

        OAuth2TokenValidatorResult result = JWTValidators.roleValidator().validate(jwt);

        assertThat(result.hasErrors()).isFalse();

        verify(jwt).getClaim(Constant.ROLE_CLAIM_NAME);
    }

    @Test
    void shouldReturnFailedResultWhenValidatingJWTTokenWithoutRoleClaim() {
        OAuth2TokenValidatorResult result = JWTValidators.roleValidator().validate(jwt);

        assertThat(result.hasErrors()).isTrue();

        verify(jwt).getClaim(Constant.ROLE_CLAIM_NAME);
    }
}