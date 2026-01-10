package com.edevare.backend.service;

import com.edevare.backend.model.Chat;
import com.edevare.backend.model.User;
import com.edevare.backend.repository.ChatRepository;
import com.edevare.backend.repository.UserRepository;
import com.edevare.shared.entitiesDTO.ChatDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    public ChatService(ChatRepository chatRepository, UserRepository userRepository) {
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
    }

    /**
     * Crea un chat si no existe, o devuelve el existente.
     */
    @Transactional
    public ChatDTO createOrGetChat(Long studentId, Long teacherId) {
        return chatRepository.findByStudentIdAndTeacherId(studentId, teacherId)
                .map(this::mapToDTO)
                .orElseGet(() -> {
                    User student = userRepository.findById(studentId)
                            .orElseThrow(() -> new RuntimeException("Student not found"));
                    User teacher = userRepository.findById(teacherId)
                            .orElseThrow(() -> new RuntimeException("Teacher not found"));

                    Chat newChat = new Chat();
                    newChat.setStudent(student);
                    newChat.setTeacher(teacher);
                    return mapToDTO(chatRepository.save(newChat));
                });
    }

    /**
     * Lista todas las conversaciones de un usuario indicando quién es la otra persona.
     */
    public List<ChatDTO> listConversations(Long userId) {
        return chatRepository.findAllUserChats(userId).stream()
                .map(chat -> {
                    ChatDTO dto = mapToDTO(chat);
                    // Lógica para identificar el nombre del interlocutor
                    String interlocutor = chat.getStudent().getId().equals(userId)
                            ? chat.getTeacher().getEmail()
                            : chat.getStudent().getEmail();

                    return new ChatDTO(
                            dto.getIdChat(),
                            dto.getIdStudent(),
                            dto.getIdTeacher(),
                            interlocutor,
                            "Click to view messages",
                            chat.getCreatedDate().toString()
                    );
                })
                .collect(Collectors.toList());
    }

    private ChatDTO mapToDTO(Chat chat) {
        return new ChatDTO(
                chat.getIdChat(),
                chat.getStudent().getId(),
                chat.getTeacher().getId(),
                null,
                null,
                chat.getCreatedDate().toString()
        );
    }
}