package com.edevare.backend.repository;

import com.edevare.backend.model.OfferClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfferClassRepository extends JpaRepository<OfferClass, Long> {

    /**
     * Permite buscar (Quien enseña la materia) o (Que materia enseña el profesor X)
     * @param idSubject Long
     * @return List
     */
    List<OfferClass>findBySubject_IdSubject(Long idSubject);

    List<OfferClass> findByTeacher_Id(Long teacherId);
}
