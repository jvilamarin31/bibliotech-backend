package com.bibliotech.backend.controller;

import com.bibliotech.backend.common.config.SecurityConfig;
import com.bibliotech.backend.common.exception.InvalidCredentialsException;
import com.bibliotech.backend.common.jwt.JwtService;
import com.bibliotech.backend.dto.user.request.LoginRequest;
import com.bibliotech.backend.dto.user.response.LoginResponse;
import com.bibliotech.backend.repository.UserRepository;
import com.bibliotech.backend.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@Import(SecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    void loginReturnsOkWithTokenWhenCredentialsAreValid() throws Exception {
        when(authService.login(any(LoginRequest.class)))
                .thenReturn(new LoginResponse("jwt-token", "admin", "Administrador", 2));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"usuario":"admin","clave":"admin123"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.usuario").value("admin"))
                .andExpect(jsonPath("$.nombre").value("Administrador"))
                .andExpect(jsonPath("$.tipoUsuario").value(2));
    }

    @Test
    void loginReturnsUnauthorizedWhenCredentialsAreInvalid() throws Exception {
        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new InvalidCredentialsException("Credenciales incorrectas"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"usuario":"admin","clave":"incorrecta"}
                                """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.mensaje").value("Credenciales incorrectas"));
    }
}
