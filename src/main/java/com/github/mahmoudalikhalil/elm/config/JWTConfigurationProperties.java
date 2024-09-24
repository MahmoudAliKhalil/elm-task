package com.github.mahmoudalikhalil.elm.config;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;

@Data
@Validated
@ConfigurationProperties("com.github.mahmoudalikhalil.jwt")
public class JWTConfigurationProperties {
    @NotNull
    private Duration notBefore = Duration.ofMinutes(2);
    @NotNull
    private Duration expiration = Duration.ofHours(24);
    @NotNull
    private Duration allowedClockSkew = Duration.ofSeconds(30);
    @NotNull
    @ToString.Exclude
    private RSAPrivateKey privateKey;
    @NotNull
    @ToString.Exclude
    private RSAPublicKey publicKey;
}

