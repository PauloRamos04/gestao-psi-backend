package com.gestaopsi.prd.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    
    @Value("${app.email.from:noreply@gestaopsi.com}")
    private String fromEmail;
    
    @Value("${app.email.enabled:false}")
    private boolean emailEnabled;
    
    @Value("${app.frontend.url:http://localhost:3000}")
    private String frontendUrl;

    @Async
    public void sendSimpleEmail(String to, String subject, String text) {
        if (!emailEnabled) {
            log.info("Email desabilitado. Email para: {} - Assunto: {}", to, subject);
            return;
        }
        
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
            log.info("Email simples enviado para: {}", to);
        } catch (Exception e) {
            log.error("Erro ao enviar email para: {}", to, e);
        }
    }

    @Async
    public void sendHtmlEmail(String to, String subject, String templateName, Map<String, Object> variables) {
        if (!emailEnabled) {
            log.info("Email desabilitado. Email HTML para: {} - Assunto: {} - Template: {}", to, subject, templateName);
            return;
        }
        
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            Context context = new Context();
            context.setVariables(variables);
            String htmlContent = templateEngine.process(templateName, context);
            
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            
            mailSender.send(message);
            log.info("Email HTML enviado para: {} usando template: {}", to, templateName);
        } catch (MessagingException e) {
            log.error("Erro ao enviar email HTML para: {}", to, e);
        }
    }

    @Async
    public void sendPasswordResetEmail(String to, String userName, String token) {
        String resetUrl = frontendUrl + "/reset-password?token=" + token;
        
        Map<String, Object> variables = Map.of(
            "userName", userName,
            "resetUrl", resetUrl,
            "frontendUrl", frontendUrl
        );
        
        sendHtmlEmail(to, "Recuperação de Senha - GestaoPsi", "password-reset", variables);
    }

    @Async
    public void sendWelcomeEmail(String to, String userName) {
        Map<String, Object> variables = Map.of(
            "userName", userName,
            "frontendUrl", frontendUrl
        );
        
        sendHtmlEmail(to, "Bem-vindo ao GestaoPsi", "welcome", variables);
    }

    @Async
    public void sendSessionReminderEmail(String to, String userName, String sessionDate, String sessionTime, String psychologistName) {
        Map<String, Object> variables = Map.of(
            "userName", userName,
            "sessionDate", sessionDate,
            "sessionTime", sessionTime,
            "psychologistName", psychologistName,
            "frontendUrl", frontendUrl
        );
        
        sendHtmlEmail(to, "Lembrete de Sessão - GestaoPsi", "session-reminder", variables);
    }

    @Async
    public void sendSessionConfirmationEmail(String to, String userName, String sessionDate, String sessionTime, String psychologistName) {
        Map<String, Object> variables = Map.of(
            "userName", userName,
            "sessionDate", sessionDate,
            "sessionTime", sessionTime,
            "psychologistName", psychologistName,
            "frontendUrl", frontendUrl
        );
        
        sendHtmlEmail(to, "Confirmação de Sessão - GestaoPsi", "session-confirmation", variables);
    }
}

