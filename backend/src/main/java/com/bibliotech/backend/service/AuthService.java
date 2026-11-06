package com.bibliotech.backend.service;

import com.bibliotech.backend.common.exception.InvalidCredentialsException;
import com.bibliotech.backend.common.jwt.JwtService;
import com.bibliotech.backend.dto.user.request.LoginRequest;
import com.bibliotech.backend.dto.user.response.LoginResponse;
import com.bibliotech.backend.entity.User;
import com.bibliotech.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {
        Optional<User> userByUsername = userRepository.findByUsername(request.username());

        if (userByUsername.isEmpty()) {
            throw new InvalidCredentialsException("Credenciales incorrectas");
        }

        User userExist = userByUsername.get();

        if (!passwordEncoder.matches(request.password(), userExist.getPassword())) {
            throw new InvalidCredentialsException("Credenciales incorrectas");
        }

        String token = jwtService.getToken(userExist);

        return new LoginResponse(
                token, userExist.getUsername(),
                userExist.getName(),
                userExist.getUserType()
        );

    }
}
