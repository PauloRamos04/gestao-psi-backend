package com.gestaopsi.prd.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(nullable = false)
    private String recurso; // PACIENTES, SESSOES, PAGAMENTOS, etc

    @Column(nullable = false)
    private Boolean ler = false;

    @Column(nullable = false)
    private Boolean criar = false;

    @Column(nullable = false)
    private Boolean editar = false;

    @Column(nullable = false)
    private Boolean deletar = false;
}

