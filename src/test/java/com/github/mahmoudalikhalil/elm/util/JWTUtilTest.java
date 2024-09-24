package com.github.mahmoudalikhalil.elm.util;

import com.github.mahmoudalikhalil.elm.config.JWTConfigurationProperties;
import com.github.mahmoudalikhalil.elm.constant.Constant;
import com.github.mahmoudalikhalil.elm.model.entity.User;
import com.github.mahmoudalikhalil.elm.model.entity.UserRole;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JWTUtilTest {
    @Spy
    private JWTConfigurationProperties jwtProperties;

    @Mock
    private JwtEncoder jwtEncoder;

    @InjectMocks
    private JWTUtil jwtUtil;

    @Test
    void shouldGenerateJWTReturnValidToken() {
        User user = User.builder()
                .id(1L)
                .role(UserRole.CLIENT)
                .build();

        String encodedToken = "encoded.jwt.token";

        Jwt jwt = when(mock(Jwt.class).getTokenValue())
                .thenReturn(encodedToken)
                .getMock();

        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwt);

        String token = jwtUtil.generateJWT(user);

        assertThat(token).isEqualTo(encodedToken);

        ArgumentCaptor<JwtEncoderParameters> captor = ArgumentCaptor.forClass(JwtEncoderParameters.class);

        verify(jwtEncoder).encode(captor.capture());

        JwtEncoderParameters parameters = captor.getValue();

        assertThat(parameters).isNotNull();

        assertThat(parameters.getJwsHeader())
                .isNotNull()
                .extracting(jwsHeader -> jwsHeader.getHeaders(), as(InstanceOfAssertFactories.MAP))
                .containsEntry("alg", SignatureAlgorithm.RS256)
                .containsEntry("typ", "JWT");

        assertThat(parameters.getClaims())
                .isNotNull()
                .satisfies(claims -> {
                    assertThat(claims.getSubject())
                            .isEqualTo("1");

                    assertThat(claims.getClaimAsString(Constant.ROLE_CLAIM_NAME))
                            .isEqualTo("CLIENT");

                    assertThat(claims.getExpiresAt())
                            .isNotNull()
                            .isInTheFuture();

                    assertThat(claims.getIssuedAt())
                            .isNotNull()
                            .isInThePast();

                    assertThat(claims.getNotBefore())
                            .isNotNull()
                            .isInThePast();
                });
    }
}