package com.edevare.backend.repository;

import com.edevare.backend.model.Rol;
import com.edevare.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
    Rol findRolByName(String name);

}
