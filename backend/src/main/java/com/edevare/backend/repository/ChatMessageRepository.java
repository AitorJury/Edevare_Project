package com.edevare.backend.repository;

import com.edevare.backend.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {


    @Query("SELECT m FROM ChatMessage m WHERE " +
            "(m.sender.id = :userId AND m.receiver.id = :user2Id) " +
            "OR " +
            "(m.sender.id = :otherUserId AND m.receiver.id = :user1Id) " +
            "ORDER BY m.createdAt ASC")
    List<ChatMessage> findChatHistory(@Param("userId") Long userId, @Param("otherUserId") Long otherUserId);
}
