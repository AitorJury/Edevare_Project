package com.edevare.backend.repository;

import com.edevare.backend.model.OfferClass;
import com.edevare.shared.entitiesDTO.OfferResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OfferClassRepository extends JpaRepository<OfferClass, Long> {

    /**
     * Permite buscar (Quien enseña la materia) o (Que materia enseña el profesor X)
     *
     * @param idSubject Long
     * @return List
     */
    List<OfferClass> findBySubject_IdSubject(Long idSubject);

    List<OfferClass> findByTeacher_Id(Long teacherId);

    List<OfferClass> getAllOfferClasses();

    @Query("SELECT o FROM OfferClass o " +
            "JOIN o.subject s " +
            "WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "AND LOWER(s.academicLevel) LIKE LOWER(CONCAT('%', :level, '%'))")
    List<OfferClass> searchBySubjectAndLevel(
            @Param("name") String name,
            @Param("level") String academicLevel
    );
}
