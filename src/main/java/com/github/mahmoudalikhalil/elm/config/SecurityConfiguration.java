package com.github.mahmoudalikhalil.elm.config;

import com.github.mahmoudalikhalil.elm.model.entity.UserRole;
import com.github.mahmoudalikhalil.elm.security.JWTValidators;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableConfigurationProperties(JWTConfigurationProperties.class)
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(authorizeRequest ->
                        authorizeRequest
                                .requestMatchers("/login", "/register", "/error", "/actuator/**")
                                .permitAll()
                                .requestMatchers("/users/**", "/products/admin", "/products/statistics")
                                .hasAuthority(UserRole.ADMIN.getAuthority())
                                .requestMatchers("/products", "/products/add", " /products/*/change-status")
                                .hasAuthority(UserRole.DEALER.getAuthority())
                                .requestMatchers("/products/user")
                                .hasAuthority(UserRole.CLIENT.getAuthority())
                                .anyRequest()
                                .authenticated()
                )
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public JwtDecoder jwtDecoder(JWTConfigurationProperties properties) {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withPublicKey(properties.getPublicKey())
                .signatureAlgorithm(SignatureAlgorithm.RS256)
                .build();

        OAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<>(
                new JwtTimestampValidator(properties.getAllowedClockSkew()),
                JWTValidators.notBeforeValidator(),
                JWTValidators.issuedAtValidator(),
                JWTValidators.expireAtValidator(),
                JWTValidators.subjectValidator(),
                JWTValidators.roleValidator()
        );

        jwtDecoder.setJwtValidator(validator);

        return jwtDecoder;
    }

    @Bean
    public JwtEncoder jwtEncoder(JWTConfigurationProperties properties) {
        RSAKey rsaKey = new RSAKey.Builder(properties.getPublicKey())
                .privateKey(properties.getPrivateKey())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new NimbusJwtEncoder(new ImmutableJWKSet<>(jwkSet));
    }
}
