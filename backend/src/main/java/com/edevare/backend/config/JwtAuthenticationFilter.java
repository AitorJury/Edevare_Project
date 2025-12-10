package com.edevare.backend.config;

import com.edevare.backend.model.User;
import com.edevare.backend.repository.UserRepository;
import com.edevare.backend.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    // Debe tener un constructor para la inyección de dependencias
    public JwtAuthenticationFilter(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    // Debe implementar el doFilterInternal
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // Extracción del Token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7); // Extraer token después de "Bearer "

        // Extracción del Email
        userEmail = jwtService.extractUsername(jwt);

        // Verificación de Contexto
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Carga y Validación del Usuario
            // Utilizamos .orElse(null) ya que userRepository.findUserByEmail puede devolver Optional.
            // Asumiendo que UserRepository devuelve Optional<User>
            User user = userRepository.findUserByEmail(userEmail).orElse(null);

            // Si el usuario existe, validar el token
            if (user != null && jwtService.isTokenValid(jwt, user)) {

                // Establecer Autenticación
                // Crear un token de autenticación
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        user,
                        null, // Credenciales nulas (ya validadas por el token)
                        Collections.emptyList()
                );

                // Añadir detalles de la web
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // Establecer el objeto de autenticación en el SecurityContextHolder
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Llama a filterChain.doFilter(...) para pasar al siguiente filtro (o al controlador)
        filterChain.doFilter(request, response);
    }
}