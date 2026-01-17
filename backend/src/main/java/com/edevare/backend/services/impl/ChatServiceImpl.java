package com.edevare.backend.services.impl;


import com.edevare.backend.model.ChatMessage;
import com.edevare.backend.model.User;
import com.edevare.backend.repository.ChatMessageRepository;
import com.edevare.backend.repository.UserRepository;
import com.edevare.backend.services.ChatService;
import com.edevare.shared.entitiesDTO.ChatMessageDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatServiceImpl(ChatMessageRepository chatMessageRepository, UserRepository userRepository, SimpMessagingTemplate messagingTemplate) {
        this.chatMessageRepository = chatMessageRepository;
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    @Transactional
    //Para guardar los mensajes en la base de datos
    public ChatMessageDTO sendMessage(ChatMessageDTO message) {

        User sender = userRepository.findById(message.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        User receiver = userRepository.findById(message.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));


        //Crear y guardar
        ChatMessage chatMessage = new ChatMessage()
                .setContentMessage(message.getContentMessage())
                .setCreatedAt(LocalDateTime.now())
                .setSender(sender)
                .setReceiver(receiver);

        //Se guarda en DB
        ChatMessage savedMessage = chatMessageRepository.save(chatMessage);

        //Se devuelve el DTO
        ChatMessageDTO responseDTO = new ChatMessageDTO(
                savedMessage.idMessage(),
                savedMessage.sender().getId(),
                savedMessage.receiver().getId(),
                savedMessage.contentMessage(),
                savedMessage.createdAt().toString()
        );

        //Enviar por websocket
        //El cliente debe estar suscrito a: /queue

        messagingTemplate.convertAndSend("/queue/messages-" + responseDTO.getReceiverId(), responseDTO);

        return responseDTO;
    }

    @Override
    public List<ChatMessageDTO> getChatHistory(Long userId, Long otherUserId) {

        return chatMessageRepository.findChatHistory(userId,otherUserId).stream()
                .map(message -> new ChatMessageDTO(
                        message.idMessage(),
                        message.sender().getId(),
                        message.receiver().getId(),
                        message.contentMessage(),
                        message.createdAt().toString()
                ))
                .toList();
    }
}
