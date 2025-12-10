package com.edevare.backend.service;

import com.edevare.backend.exceptions.RoleExistException;
import com.edevare.backend.exceptions.UserExistException;
import com.edevare.backend.model.Rol;
import com.edevare.backend.model.User;
import com.edevare.backend.repository.RolRepository;
import com.edevare.backend.repository.UserRepository;
import com.edevare.shared.entitiesDTO.UserRequestDTO;
import com.edevare.shared.entitiesDTO.UserResponseDTO;
import jakarta.transaction.Transactional;
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

    //Este constructor sirve como inyector de dependencias, mejor que el @Autowire
    public UserService(UserRepository userRepository, RolRepository rolRepository) {
        this.userRepository = userRepository;
        this.rolRepository = rolRepository;
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
        //Asignamos la contraseña hasheada
        newUser.setPasswordHash(user.getPassword());


        // 3. Asignar Rol y Guardar
        User savedUser = userRepository.save(newUser);
        return mapToDTO(savedUser);

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
