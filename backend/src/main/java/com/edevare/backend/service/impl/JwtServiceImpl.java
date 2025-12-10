package com.edevare.backend.service.impl;

import com.edevare.backend.model.User;
import com.edevare.backend.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Implementación del servicio JwtService.
 * Contiene la lógica para la generación y validación de los JWT.
 */
@Service
public class JwtServiceImpl implements JwtService {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    // Permite que el test inyecte la clave secreta y la expiración, evitando fallo de NullPointerException.
    public JwtServiceImpl(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.expiration}") long jwtExpiration) {
        this.secretKey = secretKey;
        this.jwtExpiration = jwtExpiration;
    }

    // Constructor vacío requerido por Spring.
    public JwtServiceImpl() {}

    /**
     * Genera un token JWT para un usuario.
     *
     * @param user El usuario.
     * @return El JWT.
     */
    @Override
    public String generateToken(User user) {
        return generateToken(new HashMap<>(), user);
    }

    /**
     * Genera el token JWT.
     */
    public String generateToken(
            Map<String, Object> extraClaims,
            User user
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extrae el email del token.
     */
    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Valida si un token es válido para el usuario.
     * Comprueba si el email coincide y si el token no ha expirado.
     */
    @Override
    public boolean isTokenValid(String token, User user) {
        final String username = extractUsername(token);

        return (username.equals(user.getEmail())) && !isTokenExpired(token);
    }

    /**
     * Revisa si el token ha expirado.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extrae la fecha de expiración del token.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extraer claim del token.
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Decodifica y parsea el token para obtener los claims.
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Obtiene la clave de firma a partir de la clave secreta.
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}