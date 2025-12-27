package com.edevare.backend.service;

import com.edevare.backend.model.Task;
import com.edevare.backend.model.User;
import com.edevare.backend.repository.TaskRepository;
import com.edevare.backend.repository.UserRepository;
import com.edevare.shared.entitiesDTO.TaskDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    private User user;
    private Task task;
    private TaskDTO taskDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@edevare.com");

        task = new Task();
        task.setIdTask(100L);
        task.setTitle("Estudiar Test");
        task.setPriority("ALTA");
        task.setCompleted(false);
        task.setUser(user);

        taskDTO = new TaskDTO(null, "Estudiar Test", "ALTA", false, null, 1L);
    }

    @Test
    void createTask_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskDTO savedDTO = taskService.createTask(taskDTO);

        assertNotNull(savedDTO);
        assertEquals("Estudiar Test", savedDTO.getTitle());
        assertEquals("ALTA", savedDTO.getPriority());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void updateTaskStatus_ToCompleted_Success() {
        when(taskRepository.findById(100L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(i -> i.getArguments()[0]);

        TaskDTO updatedDTO = taskService.updateTaskStatus(100L, true);

        assertTrue(updatedDTO.getCompleted());
        verify(taskRepository).save(task);
    }

    @Test
    void deleteTask_Success() {
        when(taskRepository.existsById(100L)).thenReturn(true);

        assertDoesNotThrow(() -> taskService.deleteTask(100L));

        verify(taskRepository, times(1)).deleteById(100L);
    }
}