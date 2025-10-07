package com.gestaopsi.prd.config;

import com.gestaopsi.prd.entity.LogAuditoria;
import com.gestaopsi.prd.repository.LogAuditoriaRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditAspect {

    private final LogAuditoriaRepository logRepository;

    /**
     * Intercepta todos os métodos dos controllers que fazem operações de escrita
     */
    @Around("execution(* com.gestaopsi.prd.controller..*(..)) && " +
            "(@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PutMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.DeleteMapping))")
    public Object auditControllerMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = null;
        boolean sucesso = true;
        String mensagemErro = null;

        try {
            result = joinPoint.proceed();
            return result;
        } catch (Exception e) {
            sucesso = false;
            mensagemErro = e.getMessage();
            throw e;
        } finally {
            long executionTime = System.currentTimeMillis() - startTime;
            registrarLog(joinPoint, sucesso, mensagemErro, executionTime);
        }
    }

    /**
     * Registra o log de forma assíncrona
     */
    @Async
    private void registrarLog(ProceedingJoinPoint joinPoint, boolean sucesso, String mensagemErro, long executionTime) {
        try {
            HttpServletRequest request = getHttpServletRequest();
            if (request == null) return;

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth != null && auth.isAuthenticated() ? auth.getName() : "anonymous";

            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();

            String acao = determinarAcao(method);
            String entidade = extrairEntidade(joinPoint.getSignature().getDeclaringTypeName());
            String descricao = gerarDescricao(acao, entidade, joinPoint.getArgs());

            LogAuditoria log = LogAuditoria.builder()
                .dataHora(LocalDateTime.now())
                .username(username)
                .acao(acao)
                .entidade(entidade)
                .descricao(descricao)
                .ipAddress(getClientIp(request))
                .userAgent(request.getHeader("User-Agent"))
                .metodoHttp(request.getMethod())
                .endpoint(request.getRequestURI())
                .tempoExecucaoMs(executionTime)
                .sucesso(sucesso)
                .mensagemErro(mensagemErro)
                .nivel(sucesso ? "INFO" : "ERROR")
                .modulo(determinarModulo(entidade))
                .build();

            logRepository.save(log);
            
        } catch (Exception e) {
            log.error("Erro ao registrar log de auditoria", e);
        }
    }

    private String determinarAcao(Method method) {
        if (method.isAnnotationPresent(PostMapping.class)) {
            return "CREATE";
        } else if (method.isAnnotationPresent(PutMapping.class)) {
            return "UPDATE";
        } else if (method.isAnnotationPresent(DeleteMapping.class)) {
            return "DELETE";
        }
        return "UNKNOWN";
    }

    private String extrairEntidade(String className) {
        // Ex: com.gestaopsi.prd.controller.PacientesController -> Paciente
        if (className.contains("Controller")) {
            String nomeController = className.substring(className.lastIndexOf('.') + 1);
            nomeController = nomeController.replace("Controller", "");
            
            // Remover 's' do plural se houver
            if (nomeController.endsWith("s")) {
                nomeController = nomeController.substring(0, nomeController.length() - 1);
            }
            
            // Casos especiais
            if (nomeController.equals("Sessoe")) return "Sessao";
            if (nomeController.equals("Prontuario")) return "Prontuario";
            if (nomeController.equals("Usuario")) return "Usuario";
            
            return nomeController;
        }
        return "Unknown";
    }

    private String determinarModulo(String entidade) {
        if (entidade == null) return "SISTEMA";
        
        switch (entidade.toUpperCase()) {
            case "PACIENTE": return "PACIENTES";
            case "SESSAO": return "SESSOES";
            case "PRONTUARIO": return "PRONTUARIOS";
            case "PAGAMENTO": return "PAGAMENTOS";
            case "USUARIO": return "USUARIOS";
            case "PSICOLOGO": return "PSICOLOGOS";
            case "SALA": return "SALAS";
            case "CLINICA": return "CLINICAS";
            default: return "SISTEMA";
        }
    }

    private String gerarDescricao(String acao, String entidade, Object[] args) {
        StringBuilder desc = new StringBuilder();
        
        switch (acao) {
            case "CREATE":
                desc.append("Criou ").append(entidade);
                break;
            case "UPDATE":
                desc.append("Atualizou ").append(entidade);
                if (args.length > 0 && args[0] instanceof Long) {
                    desc.append(" #").append(args[0]);
                }
                break;
            case "DELETE":
                desc.append("Deletou ").append(entidade);
                if (args.length > 0 && args[0] instanceof Long) {
                    desc.append(" #").append(args[0]);
                }
                break;
            default:
                desc.append("Ação em ").append(entidade);
        }
        
        return desc.toString();
    }

    private HttpServletRequest getHttpServletRequest() {
        try {
            ServletRequestAttributes attributes = 
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            return attributes != null ? attributes.getRequest() : null;
        } catch (Exception e) {
            return null;
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String[] headers = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_CLIENT_IP",
            "REMOTE_ADDR"
        };

        for (String header : headers) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                return ip.split(",")[0].trim();
            }
        }

        return request.getRemoteAddr();
    }
}

