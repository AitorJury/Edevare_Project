package com.edevare.backend.repository;

import com.edevare.backend.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    // Para evitar duplicar chats entre los mismos dos usuarios
    Optional<Chat> findByStudentIdAndTeacherId(Long studentId, Long teacherId);

    // Lista todos los chats donde el usuario participe (como alumno o como profesor)
    @Query("SELECT c FROM Chat c WHERE c.student.id = :userId OR c.teacher.id = :userId")
    List<Chat> findAllUserChats(@Param("userId") Long userId);
}