package com.github.mahmoudalikhalil.elm.service;

import com.github.mahmoudalikhalil.elm.mapper.UserMapper;
import com.github.mahmoudalikhalil.elm.model.dto.ClientUserRegistrationRequest;
import com.github.mahmoudalikhalil.elm.model.dto.SuperUserRegistrationRequest;
import com.github.mahmoudalikhalil.elm.model.entity.User;
import com.github.mahmoudalikhalil.elm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Transactional
    public void registerClient(ClientUserRegistrationRequest request) {
        User user = userMapper.mapClientRegistrationRequestToUser(request, passwordEncoder.encode(request.password()));
        userRepository.save(user);
    }

    @Transactional
    public void registerAdminOrDealer(SuperUserRegistrationRequest request) {
        User user = userMapper.mapSuperUserRegistrationRequestToUser(request, passwordEncoder.encode(request.password()));
        userRepository.save(user);
    }
}
