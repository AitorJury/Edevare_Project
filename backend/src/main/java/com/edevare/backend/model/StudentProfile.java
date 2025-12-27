package com.edevare.backend.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class StudentProfile {

    @Id
    @Column(name = "id_user")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id_user")
    private User user;

    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentProfile that = (StudentProfile) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
