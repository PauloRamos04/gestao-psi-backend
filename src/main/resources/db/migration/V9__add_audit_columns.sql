-- Adicionar colunas de auditoria nas tabelas principais

-- Pacientes
alter table if exists pacientes 
    add column if not exists created_at timestamp,
    add column if not exists updated_at timestamp,
    add column if not exists created_by varchar(50),
    add column if not exists updated_by varchar(50),
    add column if not exists version bigint default 0;

-- Sessões
alter table if exists sessoes 
    add column if not exists created_at timestamp,
    add column if not exists updated_at timestamp,
    add column if not exists created_by varchar(50),
    add column if not exists updated_by varchar(50),
    add column if not exists version bigint default 0,
    add column if not exists observacoes varchar(500);

-- Pagamentos
alter table if exists pagamentos 
    add column if not exists created_at timestamp,
    add column if not exists updated_at timestamp,
    add column if not exists created_by varchar(50),
    add column if not exists updated_by varchar(50),
    add column if not exists version bigint default 0,
    add column if not exists observacoes varchar(500);

-- Salas
alter table if exists salas 
    add column if not exists created_at timestamp,
    add column if not exists updated_at timestamp,
    add column if not exists created_by varchar(50),
    add column if not exists updated_by varchar(50),
    add column if not exists version bigint default 0;

-- Mensagens
alter table if exists mensagens 
    add column if not exists created_by varchar(50),
    add column if not exists updated_at timestamp,
    add column if not exists updated_by varchar(50),
    add column if not exists version bigint default 0;

-- Adicionar campos extras em pacientes
alter table if exists pacientes
    add column if not exists cpf varchar(14),
    add column if not exists email varchar(100),
    add column if not exists telefone varchar(20);

-- Índices adicionais para performance
create index if not exists idx_pacientes_nome on pacientes(nome);
create index if not exists idx_pacientes_cpf on pacientes(cpf);
create index if not exists idx_pagamentos_valor on pagamentos(valor);
create index if not exists idx_sessoes_status on sessoes(status);

-- Comentários nas tabelas
comment on table pacientes is 'Pacientes atendidos nas clínicas';
comment on table sessoes is 'Sessões agendadas e realizadas';
comment on table pagamentos is 'Pagamentos recebidos';
comment on table prontuarios is 'Prontuários eletrônicos dos pacientes';
comment on table notificacoes is 'Notificações do sistema';

