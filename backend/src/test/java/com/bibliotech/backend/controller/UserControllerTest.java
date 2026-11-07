package com.bibliotech.backend.controller;

import com.bibliotech.backend.common.config.SecurityConfig;
import com.bibliotech.backend.common.jwt.JwtService;
import com.bibliotech.backend.dto.user.request.CreateUserRequest;
import com.bibliotech.backend.dto.user.response.UserDetailResponse;
import com.bibliotech.backend.repository.UserRepository;
import com.bibliotech.backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@Import(SecurityConfig.class)
class UserControllerTest {

    private static final String CREATE_USER_BODY = """
            {"nombre":"Ana","identificacion":"123456789","tipoUsuario":1,"usuario":"ana","clave":"secret"}
            """;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    void createUserReturnsUnauthorizedWhenNoTokenIsProvided() throws Exception {
        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CREATE_USER_BODY))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(userService);
    }

    @Test
    void createUserReturnsForbiddenWhenUserHasClientRole() throws Exception {
        mockMvc.perform(post("/usuarios")
                        .with(user("cliente").authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CREATE_USER_BODY))
                .andExpect(status().isForbidden());

        verifyNoInteractions(userService);
    }

    @Test
    void createUserReturnsCreatedWhenUserHasAdminRole() throws Exception {
        when(userService.createUser(any(CreateUserRequest.class)))
                .thenReturn(new UserDetailResponse(1L, "Ana", "123456789", 1, "ana"));

        mockMvc.perform(post("/usuarios")
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CREATE_USER_BODY))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Ana"))
                .andExpect(jsonPath("$.identificacion").value("123456789"))
                .andExpect(jsonPath("$.tipoUsuario").value(1))
                .andExpect(jsonPath("$.usuario").value("ana"));

        verify(userService).createUser(any(CreateUserRequest.class));
    }
}
