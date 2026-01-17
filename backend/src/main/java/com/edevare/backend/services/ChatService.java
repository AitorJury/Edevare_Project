package com.edevare.backend.services;


import com.edevare.shared.entitiesDTO.ChatMessageDTO;

import java.util.List;

public interface ChatService {

    //Guardar el msg en la BD
    ChatMessageDTO sendMessage(ChatMessageDTO message);

    List<ChatMessageDTO> getChatHistory(Long userId, Long otherUserId);

}
