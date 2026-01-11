package com.edevare.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "chat_message")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_message")
    private Long idMessage;
    private String message;
    private String bodyMessage;

    @ManyToOne
    @JoinColumn(name = "reciver_id_user")
    private User reciver;

    @ManyToOne
    @JoinColumn(name = "sender_id_user")
    private User sender;




}
