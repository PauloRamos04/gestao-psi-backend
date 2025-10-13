package com.gestaopsi.prd.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class RateLimitFilter extends OncePerRequestFilter {

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();
    
    // Limites por endpoint
    private static final int LOGIN_LIMIT = 5; // 5 tentativas
    private static final int LOGIN_REFILL_MINUTES = 15; // a cada 15 minutos
    
    private static final int GENERAL_LIMIT = 100; // 100 requisições
    private static final int GENERAL_REFILL_MINUTES = 1; // por minuto

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        
        String key = getClientKey(request);
        Bucket bucket = resolveBucket(key, request);

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            log.warn("Rate limit excedido para: {} - Endpoint: {}", key, request.getRequestURI());
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Limite de requisições excedido. Tente novamente mais tarde.\"}");
        }
    }

    private Bucket resolveBucket(String key, HttpServletRequest request) {
        return cache.computeIfAbsent(key, k -> createNewBucket(request));
    }

    private Bucket createNewBucket(HttpServletRequest request) {
        String path = request.getRequestURI();
        
        // Limites mais rigorosos para login
        if (path.contains("/auth/login") || path.contains("/api/auth/password-reset")) {
            Bandwidth limit = Bandwidth.builder()
                .capacity(LOGIN_LIMIT)
                .refillGreedy(LOGIN_LIMIT, Duration.ofMinutes(LOGIN_REFILL_MINUTES))
                .build();
            return Bucket.builder()
                .addLimit(limit)
                .build();
        }
        
        // Limite geral para outras rotas
        Bandwidth limit = Bandwidth.builder()
            .capacity(GENERAL_LIMIT)
            .refillGreedy(GENERAL_LIMIT, Duration.ofMinutes(GENERAL_REFILL_MINUTES))
            .build();
        return Bucket.builder()
            .addLimit(limit)
            .build();
    }

    private String getClientKey(HttpServletRequest request) {
        String path = request.getRequestURI();
        String ip = getClientIP(request);
        
        // Para login, usa IP + endpoint para evitar brute force
        if (path.contains("/auth/login") || path.contains("/api/auth/password-reset")) {
            return ip + ":" + path;
        }
        
        // Para outras rotas, só IP
        return ip;
    }

    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        String path = request.getRequestURI();
        // Não aplica rate limit em rotas de saúde e swagger
        return path.startsWith("/actuator") || 
               path.startsWith("/swagger-ui") || 
               path.startsWith("/api-docs");
    }
}

