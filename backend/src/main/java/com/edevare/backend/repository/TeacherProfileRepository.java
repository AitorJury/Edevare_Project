package com.edevare.backend.repository;

import com.edevare.backend.model.TeacherProfile;
import com.edevare.backend.types.TeacherModality;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

interface TeacherProfileRepository extends JpaRepository<TeacherProfile, Long> {
    /**
     * Busca profesores por su modalidad
     * @param modality
     * @return
     */
    List<TeacherProfile> findByModality(TeacherModality modality);

    List<TeacherProfile> findByHourlyRate(BigDecimal hourlyRate);
}
