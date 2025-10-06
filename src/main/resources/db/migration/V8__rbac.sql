-- Sistema de Roles e Permissions (RBAC)

-- Tabela de Roles
create table if not exists roles (
    id serial primary key,
    nome varchar(50) unique not null,
    descricao varchar(200)
);

-- Tabela de Permissions
create table if not exists permissions (
    id serial primary key,
    role_id integer not null references roles(id),
    recurso varchar(50) not null,
    ler boolean not null default false,
    criar boolean not null default false,
    editar boolean not null default false,
    deletar boolean not null default false,
    unique(role_id, recurso)
);

-- Adicionar role_id na tabela users
alter table users add column if not exists role_id integer references roles(id);

-- Inserir Roles padrão
insert into roles (nome, descricao) values
    ('ADMIN', 'Administrador do sistema - acesso total'),
    ('CLINICA', 'Gestor da clínica - acesso administrativo'),
    ('PSICOLOGO', 'Psicólogo - acesso ao atendimento'),
    ('SECRETARIA', 'Secretária - acesso limitado')
on conflict (nome) do nothing;

-- Permissions para ADMIN (acesso total)
insert into permissions (role_id, recurso, ler, criar, editar, deletar)
select id, recurso, true, true, true, true
from roles, (values 
    ('PACIENTES'), ('SESSOES'), ('PAGAMENTOS'), ('SALAS'), 
    ('MENSAGENS'), ('USUARIOS'), ('PRONTUARIOS'), ('RELATORIOS')
) as recursos(recurso)
where nome = 'ADMIN'
on conflict do nothing;

-- Permissions para PSICOLOGO
insert into permissions (role_id, recurso, ler, criar, editar, deletar)
select id, recurso, ler, criar, editar, deletar
from roles, (values 
    ('PACIENTES', true, true, true, false),
    ('SESSOES', true, true, true, true),
    ('PAGAMENTOS', true, true, true, false),
    ('SALAS', true, false, false, false),
    ('PRONTUARIOS', true, true, true, false),
    ('RELATORIOS', true, false, false, false)
) as recursos(recurso, ler, criar, editar, deletar)
where nome = 'PSICOLOGO'
on conflict do nothing;

-- Permissions para CLINICA
insert into permissions (role_id, recurso, ler, criar, editar, deletar)
select id, recurso, ler, criar, editar, deletar
from roles, (values 
    ('PACIENTES', true, true, true, true),
    ('SESSOES', true, true, true, true),
    ('PAGAMENTOS', true, true, true, true),
    ('SALAS', true, true, true, true),
    ('USUARIOS', true, true, true, false),
    ('RELATORIOS', true, true, false, false)
) as recursos(recurso, ler, criar, editar, deletar)
where nome = 'CLINICA'
on conflict do nothing;

-- Permissions para SECRETARIA
insert into permissions (role_id, recurso, ler, criar, editar, deletar)
select id, recurso, ler, criar, editar, deletar
from roles, (values 
    ('PACIENTES', true, true, true, false),
    ('SESSOES', true, true, true, false),
    ('PAGAMENTOS', true, true, false, false),
    ('SALAS', true, false, false, false)
) as recursos(recurso, ler, criar, editar, deletar)
where nome = 'SECRETARIA'
on conflict do nothing;

-- Índices
create index if not exists idx_permissions_role on permissions(role_id);
create index if not exists idx_permissions_recurso on permissions(recurso);
create index if not exists idx_users_role on users(role_id);

