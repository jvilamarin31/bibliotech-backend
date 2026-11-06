package com.bibliotech.backend.repository;

import com.bibliotech.backend.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByIdentification(String identification);
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
    boolean existsByIdentification(String identification);

}
