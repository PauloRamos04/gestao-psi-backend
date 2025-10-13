package com.gestaopsi.prd.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
@Tag(name = "WebSocket", description = "Endpoints WebSocket para comunicação em tempo real")
public class WebSocketController {

    /**
     * Processa mensagens recebidas do cliente
     */
    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public Map<String, Object> handleMessage(Map<String, Object> message) {
        log.info("Mensagem recebida via WebSocket: {}", message);
        return Map.of(
            "type", "message",
            "data", message,
            "timestamp", System.currentTimeMillis()
        );
    }

    /**
     * Responde quando um cliente se inscreve em um tópico
     */
    @SubscribeMapping("/notifications/{userId}")
    public Map<String, Object> onSubscribeNotifications() {
        return Map.of(
            "type", "subscription",
            "message", "Conectado ao serviço de notificações",
            "timestamp", System.currentTimeMillis()
        );
    }
}


