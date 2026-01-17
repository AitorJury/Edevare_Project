package com.edevare.backend.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "chat_messages")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_message")
    private Long idMessage;

    @Column(nullable = false)
    private String contentMessage;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    public ChatMessage() {
    }

    public ChatMessage(Long idMessage, String contentMessage, LocalDateTime createdAt, User receiver, User sender) {
        this.idMessage = idMessage;
        this.createdAt = createdAt;
        this.contentMessage = contentMessage;
        this.receiver = receiver;
        this.sender = sender;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ChatMessage that = (ChatMessage) o;
        return Objects.equals(idMessage, that.idMessage) && Objects.equals(contentMessage, that.contentMessage) && Objects.equals(createdAt, that.createdAt) && Objects.equals(receiver, that.receiver) && Objects.equals(sender, that.sender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMessage, contentMessage, receiver, sender);
    }

    public Long idMessage() {
        return idMessage;
    }

    public ChatMessage setIdMessage(Long idMessage) {
        this.idMessage = idMessage;
        return this;
    }

    public String contentMessage() {
        return contentMessage;
    }

    public LocalDateTime createdAt() {
        return createdAt;
    }

    public ChatMessage setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String bodyMessage() {
        return contentMessage;
    }

    public ChatMessage setContentMessage(String bodyMessage) {
        this.contentMessage = bodyMessage;
        return this;
    }

    public User receiver() {
        return receiver;
    }

    public ChatMessage setReceiver(User receiver) {
        this.receiver = receiver;
        return this;
    }

    public User sender() {
        return sender;
    }

    public ChatMessage setSender(User sender) {
        this.sender = sender;
        return this;
    }
}
