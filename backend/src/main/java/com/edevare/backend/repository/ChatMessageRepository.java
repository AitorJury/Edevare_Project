package com.edevare.backend.repository;

import com.edevare.backend.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {


    @Query("SELECT m from ChatMessage m where m.chat.idChat=:chatId order by m.createdAt asc ")
    List<ChatMessage> finByChatId(@Param("chatId") Long chatId);
}
