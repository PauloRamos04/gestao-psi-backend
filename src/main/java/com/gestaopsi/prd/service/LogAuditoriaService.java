package com.gestaopsi.prd.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestaopsi.prd.entity.LogAuditoria;
import com.gestaopsi.prd.repository.LogAuditoriaRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogAuditoriaService {

    private final LogAuditoriaRepository logRepository;
    private final ObjectMapper objectMapper;

    /**
     * Registra log de forma assíncrona (não bloqueia operação principal)
     */
    @Async
    @Transactional
    public void registrarLog(
            String acao,
            String entidade,
            Long entidadeId,
            String descricao,
            Object dadosAnteriores,
            Object dadosNovos
    ) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth != null ? auth.getName() : "SYSTEM";
            
            HttpServletRequest request = getHttpServletRequest();
            
            LogAuditoria log = LogAuditoria.builder()
                .dataHora(LocalDateTime.now())
                .username(username)
                .acao(acao)
                .entidade(entidade)
                .entidadeId(entidadeId)
                .descricao(descricao)
                .dadosAnteriores(toJson(dadosAnteriores))
                .dadosNovos(toJson(dadosNovos))
                .ipAddress(request != null ? getClientIp(request) : null)
                .userAgent(request != null ? request.getHeader("User-Agent") : null)
                .metodoHttp(request != null ? request.getMethod() : null)
                .endpoint(request != null ? request.getRequestURI() : null)
                .sucesso(true)
                .nivel("INFO")
                .build();
            
            logRepository.save(log);
        } catch (Exception e) {
            log.error("Erro ao salvar log de auditoria", e);
        }
    }

    /**
     * Registra log simples
     */
    @Async
    @Transactional
    public void registrarLog(String acao, String entidade, Long entidadeId, String descricao) {
        registrarLog(acao, entidade, entidadeId, descricao, null, null);
    }

    /**
     * Registra erro
     */
    @Async
    @Transactional
    public void registrarErro(String descricao, Exception exception) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth != null ? auth.getName() : "SYSTEM";
            
            HttpServletRequest request = getHttpServletRequest();
            
            LogAuditoria log = LogAuditoria.builder()
                .dataHora(LocalDateTime.now())
                .username(username)
                .acao("ERROR")
                .descricao(descricao)
                .mensagemErro(exception != null ? exception.getMessage() : null)
                .ipAddress(request != null ? getClientIp(request) : null)
                .metodoHttp(request != null ? request.getMethod() : null)
                .endpoint(request != null ? request.getRequestURI() : null)
                .sucesso(false)
                .nivel("ERROR")
                .build();
            
            logRepository.save(log);
        } catch (Exception e) {
            log.error("Erro ao salvar log de erro", e);
        }
    }

    /**
     * Buscar logs com filtros
     */
    public Page<LogAuditoria> buscarComFiltros(
            Long usuarioId,
            String entidade,
            String acao,
            String modulo,
            Long clinicaId,
            LocalDateTime inicio,
            LocalDateTime fim,
            Pageable pageable
    ) {
        return logRepository.buscarComFiltros(
            usuarioId, entidade, acao, modulo, clinicaId, inicio, fim, pageable
        );
    }

    /**
     * Buscar todos logs com paginação e ordenação
     */
    public Page<LogAuditoria> buscarTodos(Pageable pageable) {
        // Verifica se há ordenação personalizada
        if (pageable.getSort().isSorted()) {
            // Se já tem ordenação, usa findAll padrão
            return logRepository.findAll(pageable);
        }
        // Por padrão, ordena por data/hora descendente (mais recentes primeiro)
        return logRepository.findAllOrderByDataHoraDesc(pageable);
    }

    /**
     * Buscar logs por usuário
     */
    public Page<LogAuditoria> buscarPorUsuario(Long usuarioId, Pageable pageable) {
        return logRepository.findByUsuarioIdOrderByDataHoraDesc(usuarioId, pageable);
    }

    /**
     * Buscar logs de uma entidade específica
     */
    public Page<LogAuditoria> buscarPorEntidade(String entidade, Pageable pageable) {
        return logRepository.findByEntidadeOrderByDataHoraDesc(entidade, pageable);
    }

    /**
     * Buscar apenas erros
     */
    public Page<LogAuditoria> buscarErros(Pageable pageable) {
        return logRepository.findBySucessoFalseOrderByDataHoraDesc(pageable);
    }

    /**
     * Estatísticas gerais
     */
    public Map<String, Object> getEstatisticas(LocalDateTime inicio) {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalAcoes", logRepository.countAcoesPorPeriodo(inicio));
        stats.put("porAcao", logRepository.countPorAcao(inicio));
        stats.put("porEntidade", logRepository.countPorEntidade(inicio));
        
        return stats;
    }

    // Métodos auxiliares
    
    private String toJson(Object obj) {
        if (obj == null) return null;
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("Erro ao converter objeto para JSON", e);
            return obj.toString();
        }
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
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
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

