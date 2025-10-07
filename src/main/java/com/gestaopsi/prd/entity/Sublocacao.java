package com.gestaopsi.prd.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "sublocacoes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sublocacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinica_id", nullable = false)
    private Clinica clinica;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "psicolog_id")
    private Psicologo psicologo;

    @Column(name = "room_name", nullable = false, length = 200)
    private String roomName;

    @Column(name = "tenant_name", nullable = false, length = 200)
    private String tenantName;

    @Column(name = "tenant_type", nullable = false, length = 30)
    private String tenantType;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "monthly_rate", nullable = false, precision = 12, scale = 2)
    private BigDecimal monthlyRate;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "total_sessions")
    private Integer totalSessions;

    @Column(name = "total_revenue", precision = 12, scale = 2)
    private BigDecimal totalRevenue;

    @Column(name = "last_payment")
    private LocalDate lastPayment;

    @Column(name = "next_payment")
    private LocalDate nextPayment;
}


