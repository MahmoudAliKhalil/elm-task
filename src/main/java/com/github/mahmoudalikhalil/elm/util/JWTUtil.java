package com.github.mahmoudalikhalil.elm.util;

import com.github.mahmoudalikhalil.elm.config.JWTConfigurationProperties;
import com.github.mahmoudalikhalil.elm.constant.Constant;
import com.github.mahmoudalikhalil.elm.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public final class JWTUtil {
    private final JWTConfigurationProperties jwtProperties;
    private final JwtEncoder jwtEncoder;

    public String generateJWT(User user) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .expiresAt(now.plus(jwtProperties.getExpiration()))
                .issuedAt(now)
                .notBefore(now.minus(jwtProperties.getNotBefore()))
                .subject(user.getId().toString())
                .claim(Constant.ROLE_CLAIM_NAME, user.getRole().getAuthority())
                .build();

        JwsHeader header = JwsHeader.with(SignatureAlgorithm.RS256)
                .header("typ", "JWT")
                .build();

        JwtEncoderParameters parameters = JwtEncoderParameters.from(header, claims);

        return jwtEncoder.encode(parameters).getTokenValue();
    }
}
