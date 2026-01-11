package com.edevare.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
//Habilita el servidor para recibir y enviar mensajes a través de WebSocket
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    //Punto de conexion inicial
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint("/ws-edevare")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }


    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        //Prefijo para mensajes que van de CLIENTE -> SERVIDOR
        config.setApplicationDestinationPrefixes("/app");

        //Prefijo para mensajes que van de SERVIDOR -> CLIENTE
        config.enableSimpleBroker("/queue");
    }
}
