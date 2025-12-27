package com.edevare.backend.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "rol")
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Long idRol;

    private String name;
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    public Long getIdRol() {
        return idRol;
    }

    public void setIdRol(Long id_rol) {
        this.idRol = id_rol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
