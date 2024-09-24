package com.github.mahmoudalikhalil.elm.service;

import com.github.mahmoudalikhalil.elm.model.dto.LoginRequest;
import com.github.mahmoudalikhalil.elm.model.dto.LoginResponse;
import com.github.mahmoudalikhalil.elm.model.entity.User;
import com.github.mahmoudalikhalil.elm.security.UserAdapter;
import com.github.mahmoudalikhalil.elm.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final JwtEncoder jwtEncoder;

    public LoginResponse login(LoginRequest request) {
        Authentication authenticationRequest =  UsernamePasswordAuthenticationToken.unauthenticated(request.username(), request.password());
        Authentication authenticationResponse = authenticationManager.authenticate(authenticationRequest);

        if (authenticationResponse.getPrincipal() instanceof UserAdapter(User user)) {
            String token = jwtUtil.generateJWT(user);
            return new LoginResponse(token);
        } else {
            throw new AuthenticationServiceException("Expecting principal of type UserAdapter but found " + authenticationResponse.getClass().getName());
        }
    }
}
