package com.edevare.backend.service;

import com.edevare.backend.config.SecurityConfig;
import com.edevare.backend.exceptions.RoleExistException;
import com.edevare.backend.exceptions.UserExistException;
import com.edevare.backend.model.Rol;
import com.edevare.backend.model.User;
import com.edevare.backend.repository.RolRepository;
import com.edevare.backend.repository.UserRepository;
import com.edevare.shared.entitiesDTO.UserRequestDTO;
import com.edevare.shared.entitiesDTO.UserResponseDTO;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Esta clase servira para llevar toda la logica del negocio de gestion de usuarios,
 * y asignacion de roles
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final RolRepository rolRepository;
    private final SecurityConfig securityConfig;
    private final JwtService jwtService;

    //Este constructor sirve como inyector de dependencias.
    public UserService(UserRepository userRepository, RolRepository rolRepository, SecurityConfig securityConfig, JwtService jwtService) {
        this.userRepository = userRepository;
        this.rolRepository = rolRepository;
        this.securityConfig = securityConfig;
        this.jwtService = jwtService;
    }

    @Transactional
    public UserResponseDTO userRegister(UserRequestDTO user) {

        // 1. Validar si el email ya existe
        if (userRepository.findUserByEmail(user.getEmail()).isPresent()) {
            throw new UserExistException("Email already exists");
        }

        Set<Rol> rolesAsigned = new HashSet<>();
        for (String roleName : user.getRoles()) {
            Rol rol = rolRepository.findRolByName(roleName);
            if (rol == null) {
                throw new RoleExistException("Role does not exist");
            }
            rolesAsigned.add(rol);
        }

        //Coversion de DTO a entidad
        User newUser = new User();
        newUser.setEmail(user.getEmail());
        //Hasheo de la contraseña
        String encodedPassword = securityConfig.passwordEncoder().encode(user.getPassword());
        //Asignamos la contraseña hasheada
        newUser.setPasswordHash(encodedPassword);


        // 3. Asignar Rol y Guardar
        User savedUser = userRepository.save(newUser);
        return mapToDTO(savedUser);

    }

    /**
     * Lógica de para el login de usuario.
     * Busca el usuario, verifica la contraseña y genera un token JWT.
     *
     * @param userRequestDTO Contiene el email y la contraseña plana.
     * @return UserResponseDTO con el token JWT en el campo passwordHash.
     */
    public UserResponseDTO userLogin(UserRequestDTO userRequestDTO) {
        // 1. Buscar usuario por email
        User user = userRepository.findUserByEmail(userRequestDTO.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Credenciales inválidas."));

        // 2. Verificar contraseña
        PasswordEncoder encoder = securityConfig.passwordEncoder();

        if (!encoder.matches(userRequestDTO.getPassword(), user.getPasswordHash())) {
            // Se usa la misma excepción para no dar pistas sobre si el email existe o no
            throw new UsernameNotFoundException("Credenciales inválidas.");
        }

        // 3. Generar token JWT
        String token = jwtService.generateToken(user);
        // Sobreescribimos el passwordHash.
        user.setPasswordHash(token);

        // 4. Mapear a DTO
        return mapToDTO(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public List<UserResponseDTO> findAll() {
        return userRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    public Rol findByRoleName(String roleName) {
        return rolRepository.findRolByName(roleName);
    }

    protected UserResponseDTO mapToDTO(User user) {
        // Convertimos el Set<Rol> a List<String> para el DTO
        List<String> roleNames = user.getRoles().stream()
                .map(Rol::getName)
                .collect(Collectors.toList());

        return new UserResponseDTO(
                user.getId(),
                user.getEmail(),
                roleNames // Pasamos la lista
        );
    }
}