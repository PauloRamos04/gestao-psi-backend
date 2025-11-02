package com.gestaopsi.prd.repository;

import com.gestaopsi.prd.entity.LogAuditoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LogAuditoriaRepository extends JpaRepository<LogAuditoria, Long> {
    
    // Buscar todos ordenado por data/hora descendente (mais recentes primeiro)
    @Query("SELECT l FROM LogAuditoria l ORDER BY l.dataHora DESC")
    Page<LogAuditoria> findAllOrderByDataHoraDesc(Pageable pageable);
    
    // Buscar todos ordenado por data/hora ascendente (mais antigos primeiro)
    @Query("SELECT l FROM LogAuditoria l ORDER BY l.dataHora ASC")
    Page<LogAuditoria> findAllOrderByDataHoraAsc(Pageable pageable);
    
    // Buscar por usuário
    Page<LogAuditoria> findByUsuarioIdOrderByDataHoraDesc(Long usuarioId, Pageable pageable);
    
    // Buscar por entidade
    Page<LogAuditoria> findByEntidadeOrderByDataHoraDesc(String entidade, Pageable pageable);
    
    // Buscar por ação
    Page<LogAuditoria> findByAcaoOrderByDataHoraDesc(String acao, Pageable pageable);
    
    // Buscar por módulo
    Page<LogAuditoria> findByModuloOrderByDataHoraDesc(String modulo, Pageable pageable);
    
    // Buscar por clínica
    Page<LogAuditoria> findByClinicaIdOrderByDataHoraDesc(Long clinicaId, Pageable pageable);
    
    // Buscar por período
    @Query("SELECT l FROM LogAuditoria l WHERE l.dataHora BETWEEN :inicio AND :fim ORDER BY l.dataHora DESC")
    Page<LogAuditoria> findByPeriodo(
        @Param("inicio") LocalDateTime inicio,
        @Param("fim") LocalDateTime fim,
        Pageable pageable
    );
    
    // Buscar logs de uma entidade específica
    @Query("SELECT l FROM LogAuditoria l WHERE l.entidade = :entidade AND l.entidadeId = :entidadeId ORDER BY l.dataHora DESC")
    List<LogAuditoria> findByEntidadeAndEntidadeId(
        @Param("entidade") String entidade,
        @Param("entidadeId") Long entidadeId
    );
    
    // Buscar com múltiplos filtros
    @Query("SELECT l FROM LogAuditoria l WHERE " +
           "(:usuarioId IS NULL OR l.usuarioId = :usuarioId) AND " +
           "(:entidade IS NULL OR l.entidade = :entidade) AND " +
           "(:acao IS NULL OR l.acao = :acao) AND " +
           "(:modulo IS NULL OR l.modulo = :modulo) AND " +
           "(:clinicaId IS NULL OR l.clinicaId = :clinicaId) AND " +
           "(:inicio IS NULL OR l.dataHora >= :inicio) AND " +
           "(:fim IS NULL OR l.dataHora <= :fim) " +
           "ORDER BY l.dataHora DESC")
    Page<LogAuditoria> buscarComFiltros(
        @Param("usuarioId") Long usuarioId,
        @Param("entidade") String entidade,
        @Param("acao") String acao,
        @Param("modulo") String modulo,
        @Param("clinicaId") Long clinicaId,
        @Param("inicio") LocalDateTime inicio,
        @Param("fim") LocalDateTime fim,
        Pageable pageable
    );
    
    // Buscar apenas erros
    Page<LogAuditoria> findBySucessoFalseOrderByDataHoraDesc(Pageable pageable);
    
    // Buscar por nível
    Page<LogAuditoria> findByNivelOrderByDataHoraDesc(String nivel, Pageable pageable);
    
    // Estatísticas
    @Query("SELECT COUNT(l) FROM LogAuditoria l WHERE l.dataHora >= :inicio")
    Long countAcoesPorPeriodo(@Param("inicio") LocalDateTime inicio);
    
    @Query("SELECT l.acao, COUNT(l) FROM LogAuditoria l WHERE l.dataHora >= :inicio GROUP BY l.acao")
    List<Object[]> countPorAcao(@Param("inicio") LocalDateTime inicio);
    
    @Query("SELECT l.entidade, COUNT(l) FROM LogAuditoria l WHERE l.dataHora >= :inicio GROUP BY l.entidade")
    List<Object[]> countPorEntidade(@Param("inicio") LocalDateTime inicio);
}

