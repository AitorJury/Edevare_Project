package com.edevare.backend.service;

import com.edevare.backend.exceptions.RoleExistException;
import com.edevare.backend.exceptions.UserExistException;
import com.edevare.backend.model.Rol;
import com.edevare.backend.model.User;
import com.edevare.backend.repository.RolRepository;
import com.edevare.backend.repository.UserRepository;
import com.edevare.shared.DTOsEntities.UserDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
    public UserDTO userRegister(UserDTO user, String roleName, String password) {
        // 1. Validar si el email ya existe
        if (userRepository.findUserByEmail(user.getEmail()).isPresent()) {
            throw new UserExistException("Email already exists");
        }
        // 2. Buscar el Rol en la BD
        Rol rol = rolRepository.findRolByName(roleName);
        if (rol == null) {
            throw new RoleExistException("Role does not exist");
        }
        //Coversion de DTO a entidad
        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setPassword_hash(password);
        newUser.setRol(rol);

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

    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Rol findByRoleName(String roleName) {
        return rolRepository.findRolByName(roleName);
    }

    protected UserDTO mapToDTO(User user) {
        return new UserDTO(
                user.getId_user(),
                user.getEmail(),
                user.getRol().getName()
        );
    }


}
