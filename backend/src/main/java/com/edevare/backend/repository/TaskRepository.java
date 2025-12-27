package com.edevare.backend.repository;

import com.edevare.backend.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUser_Id(Long userId);

    /**
     * Permite filtrar las tareas del usuario por su estado si esta completada o no
     *
     * @param userId    Long
     * @param completed boolean
     * @return List
     */
    List<Task> findByUser_IdAndCompleted(Long userId, boolean completed);
}
