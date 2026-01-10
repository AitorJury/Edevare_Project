package com.edevare.backend.controller;

import com.edevare.backend.service.ChatService;
import com.edevare.shared.entitiesDTO.ChatDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/create")
    public ResponseEntity<ChatDTO> createChat(@RequestParam Long studentId, @RequestParam Long teacherId) {
        return ResponseEntity.ok(chatService.createOrGetChat(studentId, teacherId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ChatDTO>> listChats(@PathVariable Long userId) {
        return ResponseEntity.ok(chatService.listConversations(userId));
    }
}