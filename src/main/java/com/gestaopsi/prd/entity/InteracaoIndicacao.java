package com.gestaopsi.prd.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "interacoes_indicacoes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InteracaoIndicacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, length = 120)
    private String profession;

    @Column(nullable = false, length = 120)
    private String contact;

    @Column(length = 120)
    private String location;

    @Column(length = 1000)
    private String description;

    @Column(length = 20)
    private String status;

    private LocalDate date;
}





