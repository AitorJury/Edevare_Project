package com.edevare.backend.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

@Service
public class FcmService {

    public void SimpleNotification(String token, String title, String message) {
        // Configuramos el contenido de la notificación
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(message)
                .build();

        // Preparamos el mensaje para el dispositivo concreto
        Message messageM = Message.builder()
                .setToken(token)
                .setNotification(notification)
                .build();

        try {
            // Lo enviamos
            FirebaseMessaging.getInstance().send(messageM);
            System.out.println("Notification sent successfully");
        } catch (Exception e) {
            System.err.println("Error sending notification: " + e.getMessage());
        }
    }
}