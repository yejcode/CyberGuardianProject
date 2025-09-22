package com.cyberguardian.cyberguardian_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Point d'entrée WebSocket
        registry.addEndpoint("/ws-alerts")
                .setAllowedOriginPatterns("*") // accepte toutes origines (dev)
                .withSockJS(); // fallback si WebSocket pas dispo
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Préfixes :
        // - /app → pour envoyer depuis le client vers le serveur
        // - /topic → pour diffuser les messages du serveur vers les clients
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }
}
