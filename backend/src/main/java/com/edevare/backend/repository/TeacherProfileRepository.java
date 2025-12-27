package com.edevare.backend.repository;

import com.edevare.backend.model.TeacherProfile;
import com.edevare.shared.enums.TeacherModality;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface TeacherProfileRepository extends JpaRepository<TeacherProfile, Long> {
    /**
     * Busca profesores por su modalidad
     * @param modality
     * @return
     */
    List<TeacherProfile> findByModality(TeacherModality modality);

    List<TeacherProfile> findByHourlyRate(BigDecimal hourlyRate);
}
