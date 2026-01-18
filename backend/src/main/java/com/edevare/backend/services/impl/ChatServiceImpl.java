package com.edevare.backend.services.impl;


import com.edevare.backend.model.Chat;
import com.edevare.backend.model.ChatMessage;
import com.edevare.backend.model.User;
import com.edevare.backend.repository.ChatMessageRepository;
import com.edevare.backend.repository.ChatRepository;
import com.edevare.backend.repository.UserRepository;
import com.edevare.backend.services.ChatService;
import com.edevare.shared.entitiesDTO.ChatDTO;
import com.edevare.shared.entitiesDTO.ChatMessageDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatServiceImpl implements ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatServiceImpl(ChatMessageRepository chatMessageRepository, ChatRepository chatRepository, UserRepository userRepository, SimpMessagingTemplate messagingTemplate) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;
    }


    /**
     * Crea un chat si no existe, o devuelve el existente.
     */
    @Override
    @Transactional
    public ChatDTO createOrGetChat(Long studentId, Long teacherId) {
        return chatRepository.findByStudentIdAndTeacherId(studentId, teacherId)
                .map(this::mapChatToDTO)
                .orElseGet(() -> {
                    User student = userRepository.findById(studentId)
                            .orElseThrow(() -> new RuntimeException("Student not found"));
                    User teacher = userRepository.findById(teacherId)
                            .orElseThrow(() -> new RuntimeException("Teacher not found"));

                    Chat newChat = new Chat();
                    newChat.setStudent(student);
                    newChat.setTeacher(teacher);
                    return mapChatToDTO(chatRepository.save(newChat));
                });
    }

    /**
     * Lista todas las conversaciones de un usuario indicando quién es la otra persona.
     */
    public List<ChatDTO> listConversations(Long userId) {
        return chatRepository.findAllUserChats(userId).stream()
                .map(chat -> {
                    ChatDTO dto = mapChatToDTO(chat);
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

    @Override
    @Transactional
    //Para guardar los mensajes en la base de datos
    public ChatMessageDTO sendMessage(ChatMessageDTO message) {

        User sender = userRepository.findById(message.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        //Si el DTO trae ID del chat lo buscamos por idChat si no por usuario
        Chat chat = chatRepository.findExistingChat(message.getSenderId(), message.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Chat not found"));

        //Crear mensaje y vincular a la sala
        ChatMessage chatMessage = new ChatMessage()
                .setContentMessage(message.getContentMessage())
                .setCreatedAt(LocalDateTime.now())
                .setSender(sender)
                .setChat(chat);

        ChatMessage savedMessage = chatMessageRepository.save(chatMessage);

        //Mapear a DTO
        ChatMessageDTO response = mapMessageToDTO(savedMessage, message.getReceiverId());

        //Enviar por websocket a : /queue/message-{receiverId}

        messagingTemplate.convertAndSend("/queue/message-" + message.getReceiverId(), response);

        return response;

    }

    public List<ChatMessageDTO> getChatHistory(Long chatId) {
        return chatMessageRepository.finByChatId(chatId)
                .stream()
                .map(msg -> {
                    //Consultar quien es el receptor basado en los participantes
                    Long reciverId = (msg.sender().getId().equals(msg.chat().getStudent().getId()))
                            ? msg.chat().getTeacher().getId()
                            : msg.chat().getStudent().getId();
                    return mapMessageToDTO(msg, reciverId);
                })
                .toList();
    }

    public List<ChatDTO> getUserConversations(Long userId) {
        return chatRepository.findAllUserChats(userId).stream()
                .map(this::mapChatToDTO)
                .toList();
    }

    private ChatDTO mapChatToDTO(Chat chat) {
        String lastMessage = chat.messages().stream()
                .reduce((first, second) -> second)
                .map(ChatMessage::contentMessage)
                .orElse("No messages");


        // Ajusta este constructor según como tengas definido tu ChatDTO en Kotlin
        return new ChatDTO(
                chat.getIdChat(),
                chat.getStudent().getId(),
                chat.getTeacher().getId(),
                null,
                lastMessage,
                chat.getCreatedDate().toString()
        );
    }

    private ChatMessageDTO mapMessageToDTO(ChatMessage msg, Long receiverId) {
        // Ajusta según tu ChatMessageDTO en Kotlin
        return new ChatMessageDTO(
                msg.idMessage(),
                msg.chat().getIdChat(),
                msg.sender().getId(),
                receiverId,
                msg.contentMessage(),
                msg.createdAt().toString()
        );
    }
}
