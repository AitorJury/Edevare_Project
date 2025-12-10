package com.edevare.backend.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "teacher_profile")
public class TeacherProfile {
    @Id
    @Column(name = "id_teacher")
    private Long id;//No hay que decirle que ser genere el id, porque es el que vendra del usuario
    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;
    private String description;
    private double hourlyRate;
    private String modality;//Online, presencial, hibrido

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TeacherProfile that = (TeacherProfile) o;
        return Double.compare(hourlyRate, that.hourlyRate) == 0 && Objects.equals(id, that.id) && Objects.equals(user, that.user) && Objects.equals(description, that.description) && Objects.equals(modality, that.modality);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, description, hourlyRate, modality);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public String getModality() {
        return modality;
    }

    public void setModality(String modality) {
        this.modality = modality;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
