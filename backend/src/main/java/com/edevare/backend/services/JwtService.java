package com.edevare.backend.services;

import com.edevare.backend.model.User;

/**
 * Define las operaciones para la gestión de los JWT.
 * Genera tokens, extrae y valida información de un token existente.
 */
public interface JwtService {
    /**
     * Generar token JWT después de un registro o un login.
     *
     * @param user El usuario.
     * @return El token JWT.
     */
    String generateToken(User user);

    /**
     * Extraer email del token.
     *
     * @param token El token JWT.
     * @return El email del usuario.
     */
    String extractUsername(String token);

    /**
     * Valida si un token es válido para el usuario.
     *
     * @param token El token JWT.
     * @param user  El usuario con el que se compara.
     * @return true si el token es válido y no ha expirado.
     */
    boolean isTokenValid(String token, User user);
}