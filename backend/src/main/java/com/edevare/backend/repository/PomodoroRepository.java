package com.edevare.backend.repository;

import com.edevare.backend.model.PomodoroSession;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PomodoroRepository extends JpaRepository<PomodoroSession, Long> {
    List<PomodoroSession> findByUserId(Long userId);
}