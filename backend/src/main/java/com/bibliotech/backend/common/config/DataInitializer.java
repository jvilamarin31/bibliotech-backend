package com.bibliotech.backend.common.config;

import com.bibliotech.backend.entity.User;
import com.bibliotech.backend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        if (userRepository.count() == 0) {

            User admin = User.builder()
                    .name("Administrador BiblioTech")
                    .identification("1001")
                    .userType(2) // Tipo 2: Empleado (Rol Administrador)
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .build();

            userRepository.save(admin);

            log.info("=======================================================");
            log.info("USUARIO ADMINISTRADOR CREADO AUTOMÁTICAMENTE");
            log.info("Usuario: admin");
            log.info("Contraseña: admin123");
            log.info("=======================================================");
        } else {
            log.info("La base de datos ya contiene usuarios. No se creó el admin.");
        }
    }

}
