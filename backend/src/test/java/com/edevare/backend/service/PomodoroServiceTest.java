package com.edevare.backend.service;

import com.edevare.backend.model.PomodoroSession;
import com.edevare.backend.model.User;
import com.edevare.backend.repository.PomodoroRepository;
import com.edevare.backend.repository.UserRepository;
import com.edevare.shared.entitiesDTO.PomodoroDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PomodoroServiceTest {
    @Mock
    private PomodoroRepository pomodoroRepository;
    @Mock private UserRepository userRepository;
    @InjectMocks
    private PomodoroService pomodoroService;

    @Test
    void saveSession_ShouldReturnSuggestion() {
        User user = new User();
        user.setId(1L);
        PomodoroDTO inputDTO = new PomodoroDTO(null, 1L, 25, "WORK", null, null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(pomodoroRepository.save(any())).thenAnswer(i -> {
            PomodoroSession s = i.getArgument(0);
            s.setIdSession(99L);
            return s;
        });

        PomodoroDTO result = pomodoroService.saveSession(inputDTO);

        assertNotNull(result.getMicroBreakSuggestion());
        assertEquals(99L, result.getIdSession());
        verify(pomodoroRepository).save(any());
    }
}
