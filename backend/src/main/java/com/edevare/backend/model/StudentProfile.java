package com.edevare.backend.model;

import jakarta.persistence.*;

@Entity
public class StudentProfile {

    @Id
    @Column(name = "id_student")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    private String description;

}
