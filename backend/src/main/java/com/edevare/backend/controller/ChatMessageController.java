package com.edevare.backend.controller;

import com.edevare.backend.services.ChatService;
import com.edevare.shared.entitiesDTO.ChatMessageDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
class ChatMessageController {

    private final ChatService chatService;


    ChatMessageController(ChatService chatService) {
        this.chatService = chatService;
    }


    /**
     * Este metodo se activa cuando la APP envie un mensaje a "/app/send-message"
     * El @Payload extrae atutomaticamente el JSON y lo convierte en ChatMessageDTO
     */
    @MessageMapping("/send-message")
    public void sendMessage(@Payload ChatMessageDTO message) {
        chatService.sendMessage(message);
    }
}
