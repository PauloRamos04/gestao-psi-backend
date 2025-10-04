-- Script para configurar o banco PostgreSQL para o projeto Gestão PSI
-- Execute este script como superusuário (postgres) no PostgreSQL

-- 1. Criar o banco de dados
CREATE DATABASE gestao_psi
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'Portuguese_Brazil.1252'
    LC_CTYPE = 'Portuguese_Brazil.1252'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

-- 2. Criar o usuário
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

-- 4. Conectar ao banco gestao_psi e conceder privilégios no schema public
\c gestao_psi;

-- Conceder todos os privilégios no schema public
GRANT ALL ON SCHEMA public TO gestao_psi_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO gestao_psi_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO gestao_psi_user;

-- Configurar privilégios padrão para objetos futuros
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO gestao_psi_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO gestao_psi_user;

-- Verificar se tudo foi criado corretamente
\du gestao_psi_user
\l gestao_psi
