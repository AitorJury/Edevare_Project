package com.edevare.backend.service;

import com.edevare.backend.model.PomodoroSession;
import com.edevare.backend.model.User;
import com.edevare.backend.repository.PomodoroRepository;
import com.edevare.backend.repository.UserRepository;
import com.edevare.shared.entitiesDTO.PomodoroDTO;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Random;

@Service
public class PomodoroService {
    private final PomodoroRepository pomodoroRepository;
    private final UserRepository userRepository;

    private final List<String> suggestions = List.of(
            "Realiza 5 respiraciones profundas: inhala en 4, mantén 4, exhala en 8.",
            "Estira tus brazos y gira el cuello suavemente durante 30 segundos.",
            "¡Hidratación! Bebe un vaso de agua antes de continuar.",
            "Aparta la vista de la pantalla y mira a un punto lejano por 20 segundos.",
            "Levántate y da un pequeño paseo o estira las piernas."
    );

    public PomodoroService(PomodoroRepository pomodoroRepository, UserRepository userRepository) {
        this.pomodoroRepository = pomodoroRepository;
        this.userRepository = userRepository;
    }

    public PomodoroDTO saveSession(PomodoroDTO dto) {
        User user = userRepository.findById(dto.getIdUser())
                .orElseThrow(() -> new RuntimeException("User not found"));

        PomodoroSession session = new PomodoroSession();
        session.setUser(user);
        session.setDurationMinutes(dto.getDurationMinutes());
        session.setSessionType(dto.getSessionType());

        PomodoroSession saved = pomodoroRepository.save(session);

        // Al terminar una sesión, devolvemos una sugerencia aleatoria
        String suggestion = suggestions.get(new Random().nextInt(suggestions.size()));

        return new PomodoroDTO(
                saved.getIdSession(),
                saved.getUser().getId(),
                saved.getDurationMinutes(),
                saved.getSessionType(),
                saved.getTimestamp().toString(),
                suggestion
        );
    }
}