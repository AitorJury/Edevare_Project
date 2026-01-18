package com.edevare.backend.services;

import com.edevare.backend.model.User;
import com.edevare.backend.services.impl.JwtServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas para JwtService.
 * Asegura que el servicio de JWT genera, extrae y valida correctamente los tokens.
 */
public class JwtServiceTest {
    // Instancia de la implementación.
    private JwtServiceImpl jwtService;

    private User testUser;

    // Clave secreta de prueba.
    private static final String TEST_SECRET = "2e68136b9c3b916676fd03a515d58a322e68136b9c3b916676fd03a515d58a32";

    // Tiempo de expiración para pruebas
    private static final long TEST_EXPIRATION = 86400000;

    @BeforeEach
    public void setUp() {
        // Instancia con valores.
        this.jwtService = new JwtServiceImpl(TEST_SECRET, TEST_EXPIRATION);
        // Configuramos un usuario de prueba.
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("token_user@edevare.com");
        testUser.setPasswordHash("hash_del_user");
        testUser.setRoles(Collections.emptySet());
    }

    // Caso 1: Prueba de generación de token.
    @Test
    public void generateToken() {
        String token = jwtService.generateToken(testUser);

        assertNotNull(token, "El token no debe ser nulo.");
        assertFalse(token.isEmpty(), "El token no debe ser una cadena vacía.");
        // Un token JWT válido tiene 3 partes separadas por puntos
        assertTrue(token.split("\\.").length == 3, "El token JWT debe tener 3 partes separadas por puntos.");
    }

    // Caso 2: Prueba de extracción del email.
    @Test
    public void extractUsername() {
        // Generamos el token.
        String token = jwtService.generateToken(testUser);

        String extractedEmail = jwtService.extractUsername(token);

        assertEquals(testUser.getEmail(), extractedEmail, "El email extraído debe coincidir con el del usuario.");
    }

    // Caso 3: Prueba la validación del token.
    @Test
    public void isTokenValid() {
        // Generamos el token.
        String validToken = jwtService.generateToken(testUser);

        boolean isValid = jwtService.isTokenValid(validToken, testUser);

        assertTrue(isValid, "El token generado debe ser considerado válido para el usuario.");
    }
}