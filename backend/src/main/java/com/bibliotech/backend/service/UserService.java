package com.bibliotech.backend.service;

import com.bibliotech.backend.common.exception.DuplicateResourceException;
import com.bibliotech.backend.common.exception.ForbiddenActionException;
import com.bibliotech.backend.common.exception.ResourceNotFoundException;
import com.bibliotech.backend.dto.user.request.CreateUserRequest;
import com.bibliotech.backend.dto.user.request.UpdateUserRequest;
import com.bibliotech.backend.dto.user.response.UserDetailResponse;
import com.bibliotech.backend.entity.User;
import com.bibliotech.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDetailResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new DuplicateResourceException("El nombre de usuario ya está registrado");
        }
        if (userRepository.existsByIdentification(request.identification())) {
            throw new DuplicateResourceException("El número de identificación ya está registrada");
        }

        User user = User.builder()
                .name(request.name())
                .identification(request.identification())
                .userType(request.userType())
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .build();

        User savedUser = userRepository.save(user);
        return UserDetailResponse.fromEntity(savedUser);
    }

    public List<UserDetailResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserDetailResponse::fromEntity)
                .toList();
    }

    public UserDetailResponse getUser(Long id, User authenticatedUser) {
        Optional<User> userById = userRepository.findById(id);
        if (userById.isEmpty()) {
            throw new ResourceNotFoundException("El usuario con id: "+ id +" no existe. ");
        }

        return UserDetailResponse.fromEntity(userById.get());
    }

    public UserDetailResponse updateUser(Long id, UpdateUserRequest request, User authenticatedUser) {
        Optional<User> userById = userRepository.findById(id);
        if (userById.isEmpty()) {
            throw new ResourceNotFoundException("Usuario no encontrado");
        }
        User user = userById.get();

        boolean isAdmin = authenticatedUser.getUserType() == 2;

        if (request.username() != null && !user.getUsername().equals(request.username()) && userRepository.existsByUsername(request.username())) {
            throw new DuplicateResourceException("El nombre de usuario ya está en uso");
        }
        if (request.identification() != null && !user.getIdentification().equals(request.identification()) && userRepository.existsByIdentification(request.identification())) {
            throw new DuplicateResourceException("La identificación ya está en uso");
        }

        if (request.name() != null) user.setName(request.name());
        if (request.username() != null) user.setUsername(request.username());

        if (request.identification() != null && isAdmin) {
            user.setIdentification(request.identification());
        }

        if (request.userType() != null) {
            if (isAdmin) {
                user.setUserType(request.userType());
            } else if (!request.userType().equals(user.getUserType())) {
                throw new ForbiddenActionException("No tienes permisos para modificar tu tipo de usuario.");
            }
        }

        if (request.password() != null && !request.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }

        return UserDetailResponse.fromEntity(userRepository.save(user));
    }

    public void deleteUser(Long id) {
        Optional<User> userById = userRepository.findById(id);
        if (userById.isEmpty()) {
            throw new ResourceNotFoundException("El usuario con id: "+ id +" no existe. ");
        }
        userRepository.delete(userById.get());
    }


}
