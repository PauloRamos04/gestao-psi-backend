-- Esquema inicial (mínimo) para autenticação e relacionamento básico
create table if not exists clinicas (
    id serial primary key,
    clinica_login varchar(100) not null unique,
    nome varchar(200) not null,
    status boolean not null default true,
    titulo varchar(200)
);

create table if not exists psicologos (
    id serial primary key,
    psicolog_login varchar(100) not null,
    nome varchar(200) not null,
    dt_ativacao date,
    categoria_id integer,
    unique (psicolog_login)
);

create table if not exists users (
    id serial primary key,
    clinica_id integer not null references clinicas(id),
    psicolog_id integer not null references psicologos(id),
    tipo_id integer not null,
    senha varchar(255) not null,
    status boolean not null default true,
    titulo varchar(200)
);

create index if not exists idx_users_clinica_id on users(clinica_id);
create index if not exists idx_users_psicolog_id on users(psicolog_id);


