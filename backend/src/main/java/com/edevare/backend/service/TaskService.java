package com.edevare.backend.service;

import com.edevare.backend.model.Task;
import com.edevare.backend.model.User;
import com.edevare.backend.repository.TaskRepository;
import com.edevare.backend.repository.UserRepository;
import com.edevare.shared.entitiesDTO.TaskDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(UserRepository userRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    /**
     * Metodo que sirve para crear una tareo o fallar en caso de no existir usurio
     * Devuelve un DTO para mayor seguridad
     *
     * @param taskDTO
     * @return
     */

    @Transactional
    public TaskDTO createTask(TaskDTO taskDTO) {
        // 1. Buscamos al usuario (El ID viene dentro del DTO), si no lo encuentra lanza una excepcion

        User user = userRepository.findById(taskDTO.getId_user())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. CONVERSIÓN: DTO a Entidad
        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setPriority(taskDTO.getPriority());
        task.setIs_complete(taskDTO.is_complete());
        task.setUser(user);

        // 3. Guardar en BD

        Task savedTask = taskRepository.save(task);

        // 4. CONVERSIÓN: Entidad a DTO (para devolver al frontend)

        return mapToDTO(savedTask);
    }

    public List<TaskDTO> getAllTaskFromUser(Long userID) {

        //Guardamos la lista de tareas en variable
        List<Task> tasksList = taskRepository.findTaskByIdUser(userID);

        //Convertimos las tareas a DTO
        return tasksList.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     *
     * @return Metodo que sirve para mapear la entidad tarea a clase TaskDTO
     */
    private TaskDTO mapToDTO(Task task) {
        return new TaskDTO(
                task.getId_task(),
                task.getTitle(),
                task.getPriority(),
                task.isIs_complete(),
                task.getCreated_at().toString(),
                task.getUser().getId_user()
        );
    }

}
