package com.bibliotech.backend.service;

import com.bibliotech.backend.common.exception.DuplicateResourceException;
import com.bibliotech.backend.dto.user.request.CreateUserRequest;
import com.bibliotech.backend.dto.user.response.UserDetailResponse;
import com.bibliotech.backend.entity.User;
import com.bibliotech.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void createUserThrowsDuplicateResourceWhenUsernameAlreadyExists() {
        CreateUserRequest request = buildRequest();
        when(userRepository.existsByUsername("ana")).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("El nombre de usuario ya está registrado");

        verify(userRepository, never()).existsByIdentification(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUserThrowsDuplicateResourceWhenIdentificationAlreadyExists() {
        CreateUserRequest request = buildRequest();
        when(userRepository.existsByUsername("ana")).thenReturn(false);
        when(userRepository.existsByIdentification("123456789")).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("El número de identificación ya está registrada");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUserSavesEncodedPasswordWhenDataIsValid() {
        CreateUserRequest request = buildRequest();
        when(userRepository.existsByUsername("ana")).thenReturn(false);
        when(userRepository.existsByIdentification("123456789")).thenReturn(false);
        when(passwordEncoder.encode("secret")).thenReturn("encoded-secret");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(10L);
            return user;
        });

        UserDetailResponse response = userService.createUser(request);

        assertThat(response.id()).isEqualTo(10L);
        assertThat(response.name()).isEqualTo("Ana");
        assertThat(response.identification()).isEqualTo("123456789");
        assertThat(response.userType()).isEqualTo(1);
        assertThat(response.username()).isEqualTo("ana");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        assertThat(userCaptor.getValue().getPassword()).isEqualTo("encoded-secret");
    }

    private CreateUserRequest buildRequest() {
        return new CreateUserRequest(
                "Ana",
                "123456789",
                1,
                "ana",
                "secret"
        );
    }
}
