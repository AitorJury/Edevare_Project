package com.edevare.backend.service;

import com.edevare.backend.config.SecurityConfig;
import com.edevare.backend.exceptions.RoleExistException;
import com.edevare.backend.exceptions.UserExistException;
import com.edevare.backend.model.Rol;
import com.edevare.backend.model.User;
import com.edevare.backend.repository.RolRepository;
import com.edevare.backend.repository.UserRepository;
import com.edevare.shared.entitiesDTO.UserRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

// Usamos Mockito para inicializar los mocks.
public class UserServiceTest {
    // La clase a probar.
    private UserService userService;

    // Dependencias que simulamos (Mocks).
    @Mock
    private UserRepository userRepository;
    @Mock
    private RolRepository rolRepository;
    @Mock
    private SecurityConfig securityConfig;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;

    // Configuración inicial antes de cada prueba.
    @BeforeEach
    public void setUp() {
        // Inicializamos los mocks.
        MockitoAnnotations.openMocks(this);
        // Aseguramos que securityConfig.passwordEncoder() devuelva el mock de encoder.
        when(securityConfig.passwordEncoder()).thenReturn(passwordEncoder);
        this.userService = new UserService(userRepository, rolRepository, securityConfig,  jwtService);
    }

    // Caso 1: Prueba de registro correcto.
    @Test
    public void userRegisterCorrect() {
        String userEmail = "test@edevare.com";
        String rawPassword = "securePassword123";
        String encodedPassword = "ENCODED_HASH";

        // DTO de entrada para la prueba.
        UserRequestDTO requestDTO = new UserRequestDTO(
                userEmail,
                rawPassword,
                Collections.singletonList("STUDENT")
        );

        // Simular que el usuario no existe.
        when(userRepository.findUserByEmail(userEmail)).thenReturn(Optional.empty());

        // Simular que el rol existe.
        Rol studentRol = new Rol();
        studentRol.setName("STUDENT");
        when(rolRepository.findRolByName(anyString())).thenReturn(studentRol);

        // Simular el cifrado de contraseña.
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        // Simular el guardado en BBDD.
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setEmail(userEmail);
        savedUser.setPasswordHash(encodedPassword);
        savedUser.setRoles(Set.of(studentRol));

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // No debe lanzar excepciones.
        assertDoesNotThrow(() -> userService.userRegister(requestDTO));

        // Se intenta guardar el usuario y se cifra la contraseña una vez.
        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode(rawPassword);
    }

    // Caso 2: Prueba de usuario existente.
    @Test
    public void userRegisterExistingUser() {
        UserRequestDTO requestDTO = new UserRequestDTO(
                "existing@edevare.com",
                "securePassword123",
                Collections.singletonList("STUDENT")
        );

        // Simular que el usuario ya existe.
        User existingUser = new User();
        when(userRepository.findUserByEmail("existing@edevare.com")).thenReturn(Optional.of(existingUser));

        // Simular el rol.
        Rol studentRol = new Rol();
        studentRol.setName("STUDENT");
        when(rolRepository.findRolByName(anyString())).thenReturn(studentRol);

        // Verificamos que se lance la excepción 'UserExistException'.
        assertThrows(UserExistException.class, () -> userService.userRegister(requestDTO));

        // No se intenta guardar el usuario.
        verify(userRepository, never()).save(any(User.class));
    }
}