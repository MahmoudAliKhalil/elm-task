package com.github.mahmoudalikhalil.elm.repository;

import com.github.mahmoudalikhalil.elm.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
