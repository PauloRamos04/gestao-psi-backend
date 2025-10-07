package com.gestaopsi.prd.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "download_requests")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DownloadRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "inicio")
    private String inicio;

    @Column(name = "fim")
    private String fim;

    @Column(name = "status", nullable = false)
    @Builder.Default
    private String status = "pending"; // pending, processing, completed, failed

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "user_id")
    private Long userId;
}

