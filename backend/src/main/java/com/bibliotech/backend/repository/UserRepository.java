package com.bibliotech.backend.repository;

import com.bibliotech.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByIdentification(String identification);
    Optional<User> findByUsername(String username);
}
