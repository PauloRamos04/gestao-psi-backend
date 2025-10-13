package com.gestaopsi.prd.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "permissions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String nome; // Ex: usuarios.criar, pacientes.editar, sessoes.deletar

    @Column(length = 200)
    private String descricao;

    @Column(length = 50)
    private String modulo; // Ex: usuarios, pacientes, sessoes, pagamentos, etc

    @Column(length = 50)
    private String acao; // Ex: criar, ler, editar, deletar, exportar

    @Column(nullable = false)
    @Builder.Default
    private Boolean ativo = true;

    @ManyToMany(mappedBy = "permissions")
    @Builder.Default
    private Set<Role> roles = new HashSet<>();
}
