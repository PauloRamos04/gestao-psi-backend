package com.gestaopsi.prd.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestaopsi.prd.service.SystemConfigService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Filtro que verifica se o sistema está em modo de manutenção
 * Bloqueia todas as requisições exceto para usuários ADMINISTRADOR
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class MaintenanceModeFilter extends OncePerRequestFilter {

    private final SystemConfigService systemConfigService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // Permitir sempre endpoints de autenticação e saúde
        String path = request.getRequestURI();
        if (isPublicEndpoint(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Verificar se está em modo de manutenção
        try {
            Boolean maintenanceMode = systemConfigService.isMaintenanceMode();
            
            if (maintenanceMode != null && maintenanceMode) {
                // Verificar se o usuário é ADMINISTRADOR
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                
                if (authentication != null && authentication.isAuthenticated()) {
                    boolean isAdmin = authentication.getAuthorities().stream()
                        .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMINISTRADOR") || 
                                         auth.getAuthority().equals("ROLE_ADMIN"));
                    
                    if (isAdmin) {
                        // Admin pode acessar mesmo em manutenção
                        log.debug("Admin acessando durante modo de manutenção: {}", authentication.getName());
                        filterChain.doFilter(request, response);
                        return;
                    }
                }
                
                // Bloquear acesso com mensagem de manutenção
                log.warn("Acesso bloqueado - Sistema em modo de manutenção: {}", path);
                response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "Sistema em Manutenção");
                errorResponse.put("message", "O sistema está temporariamente indisponível para manutenção. Por favor, tente novamente mais tarde.");
                errorResponse.put("status", 503);
                errorResponse.put("timestamp", System.currentTimeMillis());
                
                response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
                return;
            }
            
        } catch (Exception e) {
            log.error("Erro ao verificar modo de manutenção", e);
            // Em caso de erro, deixa passar (fail-open para não bloquear o sistema)
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Verifica se o endpoint é público e deve ser sempre acessível
     */
    private boolean isPublicEndpoint(String path) {
        return path.startsWith("/api/auth/") ||
               path.startsWith("/api/health") ||
               path.contains("/swagger") ||
               path.contains("/api-docs") ||
               path.contains("/h2-console");
    }
}

