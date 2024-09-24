package com.github.mahmoudalikhalil.elm.security;

import com.github.mahmoudalikhalil.elm.constant.Constant;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.jwt.JwtClaimValidator;

import java.util.Objects;

public final class JWTValidators {
    private JWTValidators() {
        throw new IllegalCallerException("Utility class cannot be instantiated.");
    }

    public static OAuth2TokenValidator<Jwt> notBeforeValidator() {
        return new JwtClaimValidator<>(JwtClaimNames.NBF, Objects::nonNull);
    }

    public static OAuth2TokenValidator<Jwt> issuedAtValidator() {
        return new JwtClaimValidator<>(JwtClaimNames.IAT, Objects::nonNull);
    }

    public static OAuth2TokenValidator<Jwt> expireAtValidator() {
        return new JwtClaimValidator<>(JwtClaimNames.EXP, Objects::nonNull);
    }

    public static OAuth2TokenValidator<Jwt> subjectValidator() {
        return new JwtClaimValidator<>(JwtClaimNames.SUB, Objects::nonNull);
    }

    public static OAuth2TokenValidator<Jwt> roleValidator() {
        return new JwtClaimValidator<>(Constant.ROLE_CLAIM_NAME, Objects::nonNull);
    }
}
