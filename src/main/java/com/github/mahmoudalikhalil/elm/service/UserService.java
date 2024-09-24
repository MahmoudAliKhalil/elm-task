package com.github.mahmoudalikhalil.elm.service;

import com.github.mahmoudalikhalil.elm.exception.AdminSelfUpdateException;
import com.github.mahmoudalikhalil.elm.exception.UserNotFoundException;
import com.github.mahmoudalikhalil.elm.model.entity.User;
import com.github.mahmoudalikhalil.elm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public void changeUserStatus(Long userId, Principal principal) {
        User user = userRepository.findById(userId).
                orElseThrow(UserNotFoundException::new);

        Long currentUserId = Long.valueOf(principal.getName());
        if (userId.equals(currentUserId)) {
            throw new AdminSelfUpdateException();
        }

        user.setStatus(user.getStatus().flipStatus());
    }
}
