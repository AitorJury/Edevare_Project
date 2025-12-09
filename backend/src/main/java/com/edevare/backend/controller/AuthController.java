package com.edevare.backend.controller;

import com.edevare.backend.service.UserService;
import com.edevare.shared.entitiesDTO.UserRequestDTO;
import com.edevare.shared.entitiesDTO.UserResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    // Inyeccion de dependencias
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Endpoint para registro de usuarios
     * Se usa ResponseEntity para poder devolver un mensaje claro del servidor cuando se crea
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO newUser = userService.userRegister(userRequestDTO);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    /**
     * Endpoint para autenticación de usuarios.
     * Si las credenciales son válidas, devuelve el UserResponseDTO que contiene
     * el JWT en el campo 'passwordHash'.
     */
    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> loginUser(@RequestBody UserRequestDTO userRequestDTO){
        UserResponseDTO loggedInUser = userService.userLogin(userRequestDTO);
        return new ResponseEntity<>(loggedInUser, HttpStatus.OK);
    }
}
