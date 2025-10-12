package com.gestaopsi.prd.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "interacoes_sugestoes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InteracaoSugestao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 2000)
    private String content;

    @Column(length = 20)
    private String type;

    @Column(length = 20)
    private String status;

    @Column(length = 10)
    private String priority;

    @Column(length = 100)
    private String author;

    private LocalDate date;

    @Column(length = 1000)
    private String response;

    private LocalDate responseDate;
}




