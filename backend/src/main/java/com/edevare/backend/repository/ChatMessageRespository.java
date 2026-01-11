package com.edevare.backend.repository;

import com.edevare.backend.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRespository extends JpaRepository<ChatMessage, Long> {
}
