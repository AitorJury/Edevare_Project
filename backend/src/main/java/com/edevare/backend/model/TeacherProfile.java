package com.edevare.backend.model;

import com.edevare.shared.enums.TeacherModality;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "teacher_profile")
public class TeacherProfile {
    //No hay que decirle que ser genere el id, porque es el que vendra del usuario
    @Id
    @Column(name = "id_user")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id_user")
    private User user;

    private String description;

    private BigDecimal hourlyRate;

    @Enumerated(EnumType.STRING)
    private TeacherModality modality;//Online, presencial, hibrido

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OfferClass> offerClasses = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeacherProfile that = (TeacherProfile) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public BigDecimal getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(BigDecimal hourlyRate) {
        this.hourlyRate = hourlyRate;
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

    public TeacherModality getModality() {
        return modality;
    }

    public void setModality(TeacherModality modality) {
        this.modality = modality;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<OfferClass> getOfferClasses() {
        return offerClasses;
    }

    public void setOfferClasses(List<OfferClass> offerClasses) {
        this.offerClasses = offerClasses;
    }
}
