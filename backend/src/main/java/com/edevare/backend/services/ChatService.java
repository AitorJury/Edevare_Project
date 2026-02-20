package com.edevare.backend.services;


import com.edevare.shared.entitiesDTO.ChatDTO;
import com.edevare.shared.entitiesDTO.ChatMessageDTO;

import java.util.List;

public interface ChatService {

    //Logica de salas
    //Crea una sala si no existe, o devuleve la existente
    ChatDTO createOrGetChat(Long studentId, Long teacherId);

    //Lista todas las salas de un usuario
    List<ChatDTO> listConversations(Long userId);


    //Logica de mensajes
    //Guardar el msg en la BD
    ChatMessageDTO sendMessage(ChatMessageDTO message);

    List<ChatMessageDTO> getChatHistory(Long chatId);

}
