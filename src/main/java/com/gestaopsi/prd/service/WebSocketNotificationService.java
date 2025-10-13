package com.gestaopsi.prd.service;

import com.gestaopsi.prd.entity.Notificacao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebSocketNotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Envia notificação para um usuário específico
     */
    public void sendNotificationToUser(Long usuarioId, Notificacao notificacao) {
        try {
            messagingTemplate.convertAndSend(
                "/topic/notifications/" + usuarioId,
                notificacao
            );
            log.info("Notificação enviada via WebSocket para usuário: {}", usuarioId);
        } catch (Exception e) {
            log.error("Erro ao enviar notificação via WebSocket", e);
        }
    }

    /**
     * Envia notificação para todos os usuários de uma clínica
     */
    public void sendNotificationToClinic(Long clinicaId, Notificacao notificacao) {
        try {
            messagingTemplate.convertAndSend(
                "/topic/notifications/clinic/" + clinicaId,
                notificacao
            );
            log.info("Notificação enviada via WebSocket para clínica: {}", clinicaId);
        } catch (Exception e) {
            log.error("Erro ao enviar notificação via WebSocket", e);
        }
    }

    /**
     * Envia notificação broadcast (todos os usuários)
     */
    public void sendBroadcastNotification(Notificacao notificacao) {
        try {
            messagingTemplate.convertAndSend(
                "/topic/notifications/broadcast",
                notificacao
            );
            log.info("Notificação broadcast enviada via WebSocket");
        } catch (Exception e) {
            log.error("Erro ao enviar notificação broadcast via WebSocket", e);
        }
    }

    /**
     * Envia atualização de agenda
     */
    public void sendAgendaUpdate(Long psicologoId, Map<String, Object> update) {
        try {
            messagingTemplate.convertAndSend(
                "/topic/agenda/" + psicologoId,
                update
            );
            log.info("Atualização de agenda enviada via WebSocket para psicólogo: {}", psicologoId);
        } catch (Exception e) {
            log.error("Erro ao enviar atualização de agenda via WebSocket", e);
        }
    }

    /**
     * Envia atualização de sistema (manutenção, avisos, etc)
     */
    public void sendSystemUpdate(Map<String, Object> update) {
        try {
            messagingTemplate.convertAndSend(
                "/topic/system/updates",
                update
            );
            log.info("Atualização de sistema enviada via WebSocket");
        } catch (Exception e) {
            log.error("Erro ao enviar atualização de sistema via WebSocket", e);
        }
    }
}

