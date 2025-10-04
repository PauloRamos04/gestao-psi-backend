-- Script ETL para migrar dados do MySQL para PostgreSQL
-- Execute este script após configurar as credenciais do MySQL legado

-- 1. Exportar dados do MySQL (execute no MySQL)
-- mysqldump -u usuario -p gestao_psi --no-create-info --complete-insert > dados_mysql.sql

-- 2. Ajustar sintaxe para PostgreSQL
-- Substituir AUTO_INCREMENT por SERIAL
-- Substituir DATETIME por TIMESTAMP
-- Substituir TINYINT(1) por BOOLEAN
-- Ajustar aspas simples para aspas duplas se necessário

-- 3. Exemplo de migração de dados (ajustar conforme estrutura real)

-- Migrar clínicas
INSERT INTO clinicas (clinica_login, nome, status, titulo)
SELECT clinica_login, nome, status, titulo FROM mysql_clinicas;

-- Migrar psicólogos
INSERT INTO psicologos (psicolog_login, nome, dt_ativacao, categoria_id)
SELECT psicolog_login, nome, dt_ativacao, categoria_id FROM mysql_psicologos;

-- Migrar usuários
INSERT INTO users (clinica_id, psicolog_id, tipo_id, senha, status, titulo)
SELECT clinica_id, psicolog_id, tipo_id, senha, status, titulo FROM mysql_users;

-- Migrar pacientes
INSERT INTO pacientes (clinica_id, psicolog_id, nome, status)
SELECT clinica_id, psicolog_id, nome, status FROM mysql_pacientes;

-- Migrar salas
INSERT INTO salas (clinica_id, nome)
SELECT clinica_id, nome FROM mysql_salas;

-- Migrar sessões
INSERT INTO sessoes (clinica_id, psicolog_id, paciente_id, sala_id, data, hora, status)
SELECT clinica_id, psicolog_id, paciente_id, sala_id, data, hora, status FROM mysql_sessoes;

-- Migrar pagamentos
INSERT INTO pagamentos (clinica_id, psicolog_id, paciente_id, valor, data, tipo_pagamento_id)
SELECT clinica_id, psicolog_id, paciente_id, valor, data, tipo_pagamento_id FROM mysql_pagamentos;

-- Migrar mensagens
INSERT INTO mensagens (titulo, conteudo, data_criacao, status)
SELECT titulo, conteudo, data_criacao, status FROM mysql_mensagens;

-- Verificar integridade
SELECT 'Clinicas migradas: ' || COUNT(*) FROM clinicas;
SELECT 'Psicologos migrados: ' || COUNT(*) FROM psicologos;
SELECT 'Usuarios migrados: ' || COUNT(*) FROM users;
SELECT 'Pacientes migrados: ' || COUNT(*) FROM pacientes;
SELECT 'Sessoes migradas: ' || COUNT(*) FROM sessoes;
SELECT 'Pagamentos migrados: ' || COUNT(*) FROM pagamentos;
