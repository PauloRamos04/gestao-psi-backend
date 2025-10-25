package com.gestaopsi.prd.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "system_config")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SystemConfig extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "config_key", unique = true, nullable = false)
    private String configKey;

    @Column(name = "config_value", columnDefinition = "TEXT")
    private String configValue;

    @Column(name = "config_type")
    private String configType; // STRING, INTEGER, BOOLEAN, JSON

    @Column(name = "description")
    private String description;

    @Column(name = "category")
    private String category; // SYSTEM, EMAIL, SECURITY, NOTIFICATIONS, BACKUP, LOGS

    @Column(name = "is_encrypted")
    private Boolean isEncrypted = false;
}




