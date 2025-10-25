package com.gestaopsi.prd.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import jakarta.annotation.PostConstruct;

@Configuration
@Profile("prod")
public class DatabaseConfigDebug {
    
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfigDebug.class);
    
    @Value("${spring.datasource.url:NOT_SET}")
    private String datasourceUrl;
    
    @Value("${spring.datasource.username:NOT_SET}")
    private String datasourceUsername;
    
    @Value("${spring.datasource.password:NOT_SET}")
    private String datasourcePassword;
    
    @Value("${DATABASE_URL:NOT_SET}")
    private String databaseUrl;
    
    @Value("${DATABASE_USER:NOT_SET}")
    private String databaseUser;
    
    @Value("${DB_PASSWORD:NOT_SET}")
    private String dbPassword;
    
    @Value("${PGHOST:NOT_SET}")
    private String pgHost;
    
    @Value("${PGPORT:NOT_SET}")
    private String pgPort;
    
    @Value("${PGDATABASE:NOT_SET}")
    private String pgDatabase;
    
    @Value("${PGUSER:NOT_SET}")
    private String pgUser;
    
    @Value("${PGPASSWORD:NOT_SET}")
    private String pgPassword;
    
    @PostConstruct
    public void logDatabaseConfig() {
        logger.info("=== DATABASE CONFIGURATION DEBUG ===");
        logger.info("spring.datasource.url: {}", datasourceUrl);
        logger.info("spring.datasource.username: {}", datasourceUsername);
        logger.info("spring.datasource.password: {}", datasourcePassword);
        logger.info("DATABASE_URL: {}", databaseUrl);
        logger.info("DATABASE_USER: {}", databaseUser);
        logger.info("DB_PASSWORD: {}", dbPassword);
        logger.info("PGHOST: {}", pgHost);
        logger.info("PGPORT: {}", pgPort);
        logger.info("PGDATABASE: {}", pgDatabase);
        logger.info("PGUSER: {}", pgUser);
        logger.info("PGPASSWORD: {}", pgPassword);
        logger.info("=====================================");
    }
}
