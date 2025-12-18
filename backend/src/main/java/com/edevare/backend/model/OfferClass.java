package com.edevare.backend.model;

import com.edevare.backend.types.OfferClassState;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class OfferClass {

    @Id
    @Column(name = "id_offer_class")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOfferClass;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_teacher", nullable = false)
    private TeacherProfile teacher;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_subject", nullable = false)
    private Subject subject;

    @Column(name = "title_class")
    private String titleClass;

    //Enum para los estados de las clases
    @Enumerated(EnumType.STRING)
    private OfferClassState state;

    @Column(name = "price_class")
    private BigDecimal priceClass;

    public Long getIdOfferClass() {
        return idOfferClass;
    }

    public void setIdOfferClass(Long idOfferClass) {
        this.idOfferClass = idOfferClass;
    }

    public TeacherProfile getTeacher() {
        return teacher;
    }

    public void setTeacher(TeacherProfile teacher) {
        this.teacher = teacher;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public String getTitleClass() {
        return titleClass;
    }

    public void setTitleClass(String titleClass) {
        this.titleClass = titleClass;
    }

    public OfferClassState getState() {
        return state;
    }

    public void setState(OfferClassState state) {
        this.state = state;
    }

    public BigDecimal getPriceClass() {
        return priceClass;
    }

    public void setPriceClass(BigDecimal priceClass) {
        this.priceClass = priceClass;
    }
}
