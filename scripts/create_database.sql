-- Script para criar o banco e usuário do projeto
-- Execute este script conectado como postgres no pgAdmin

-- 1. Criar o banco de dados
CREATE DATABASE gestao_psi
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

-- 2. Criar o usuário específico do projeto
CREATE USER gestao_psi_user WITH
    LOGIN
    NOSUPERUSER
    NOCREATEDB
    NOCREATEROLE
    INHERIT
    NOREPLICATION
    CONNECTION LIMIT -1
    PASSWORD 'gestao_psi_pass';

-- 3. Conceder privilégios
GRANT ALL PRIVILEGES ON DATABASE gestao_psi TO gestao_psi_user;

-- 4. Conectar ao banco e configurar permissões
\c gestao_psi;

GRANT ALL ON SCHEMA public TO gestao_psi_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO gestao_psi_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO gestao_psi_user;

-- Configurar privilégios padrão para objetos futuros
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO gestao_psi_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO gestao_psi_user;

-- Verificar se foi criado
\du gestao_psi_user
\l gestao_psi
