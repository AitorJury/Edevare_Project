package com.edevare.backend.repository;

import com.edevare.backend.model.TeacherProfile;
import org.springframework.data.jpa.repository.JpaRepository;

interface TeacherProfileRepository extends JpaRepository<TeacherProfile, Long> {
}
