package com.edevare.backend.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chats")
public class Chat {

    public Chat(Long idChat, User student, User teacher, LocalDateTime createdDate) {
        this.idChat = idChat;
        this.student = student;
        this.teacher = teacher;
        this.createdDate = createdDate;
    }

    public Chat() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_chat")
    private Long idChat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_student", nullable = false)
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_teacher", nullable = false)
    private User teacher;

    @Column(name = "created_at")
    private LocalDateTime createdDate = LocalDateTime.now();

    // Relacion con mensajes para poder acceder a los mensajes de un chat
    @OneToMany(mappedBy = "chat",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> messages = new ArrayList<>();

    public List<ChatMessage> messages() {
        return messages;
    }

    public Chat setMessages(List<ChatMessage> messages) {
        this.messages = messages;
        return this;
    }

    // Getters y Setters
    public Long getIdChat() {
        return idChat;
    }

    public void setIdChat(Long idChat) {
        this.idChat = idChat;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}