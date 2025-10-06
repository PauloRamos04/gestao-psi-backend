package com.gestaopsi.prd.repository;

import com.gestaopsi.prd.entity.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {
    
    @Query("SELECT n FROM Notificacao n WHERE n.usuario.id = :usuarioId AND n.lida = false ORDER BY n.dataCriacao DESC")
    List<Notificacao> findByUsuarioIdAndLidaFalseOrderByDataCriacaoDesc(@Param("usuarioId") Long usuarioId);
    
    @Query("SELECT n FROM Notificacao n WHERE n.usuario.id = :usuarioId ORDER BY n.dataCriacao DESC")
    List<Notificacao> findByUsuarioIdOrderByDataCriacaoDesc(@Param("usuarioId") Long usuarioId);
    
    @Query("SELECT COUNT(n) FROM Notificacao n WHERE n.usuario.id = :usuarioId AND n.lida = false")
    Long countByUsuarioIdAndLidaFalse(@Param("usuarioId") Long usuarioId);
}

