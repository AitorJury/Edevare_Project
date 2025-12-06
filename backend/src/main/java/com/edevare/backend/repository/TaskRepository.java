package com.edevare.backend.repository;

import com.edevare.backend.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    //Metodo que sirve para buscar todas las tareas del usuario
    List<Task> findTaskByIdUser(Long id_user);
}
