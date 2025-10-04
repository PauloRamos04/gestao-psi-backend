package com.gestaopsi.prd.repository;

import com.gestaopsi.prd.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query(value = "select u.* from users u\n" +
            "join clinicas c on c.id = u.clinica_id and c.status = true\n" +
            "join psicologos p on p.id = u.psicolog_id\n" +
            "where u.status = true and c.clinica_login = :clinLogin and p.psicolog_login = :psiLogin",
            nativeQuery = true)
    Optional<Usuario> findActiveUserByLogins(@Param("clinLogin") String clinLogin, @Param("psiLogin") String psiLogin);

    @Transactional
    @Modifying
    @Query(value = "update users set status = true where id = :userId", nativeQuery = true)
    int ativar(@Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query(value = "update users set status = false where id = :userId", nativeQuery = true)
    int desativar(@Param("userId") Long userId);
}


