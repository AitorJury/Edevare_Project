package com.edevare.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy; // Importación necesaria
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // Importación necesaria

@Configuration
public class SecurityConfig {

    // Inyección y Constructor
    private final JwtAuthenticationFilter jwtAuthFilter;

    // Constructor para inyección de dependencia
    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    // Configuración de la Cadena de Seguridad
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Desactivamos CSRF porque usaremos Tokens (JWT)
                .authorizeHttpRequests(auth -> auth
                        // Permitimos el acceso a lo que venga de /auth/** (Login, Registro)
                        .requestMatchers("/auth/**").permitAll()
                        // El resto de peticiones (ej: /tasks) requieren autenticación
                        .anyRequest().authenticated()
                )
                // Configurar Sesiones: Política STATELESS
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // Añadir Filtro JWT: Se ejecuta antes del filtro estándar de autenticación
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Añadir Bean AuthenticationManager
    // Este bean es necesario para poder autenticar al usuario usando el AuthenticationManager
    // en el UserService (o cualquier otro servicio de autenticación).
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}