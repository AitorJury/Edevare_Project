package com.edevare.backend.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_subject")
    private Long idSubject;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * Campo para filtrar si la materia es de (Primaria,ESO,Bachiller)
     */
    @Column(name = "academic_level")
    private String academicLevel;

    /**
     * Relación inversa: Permite saber qué ofertas existen para esta materia
     */
    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OfferClass> offerClasses = new ArrayList<>();

    public Long getIdSubject() {
        return idSubject;
    }

    public void setIdSubject(Long idSubject) {
        this.idSubject = idSubject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAcademicLevel() {
        return academicLevel;
    }

    public void setAcademicLevel(String academicLevel) {
        this.academicLevel = academicLevel;
    }

    public List<OfferClass> getOfferClasses() {
        return offerClasses;
    }

    public void setOfferClasses(List<OfferClass> offerClasses) {
        this.offerClasses = offerClasses;
    }
}
