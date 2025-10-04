create table if not exists categorias (
    id serial primary key,
    nome varchar(100) not null
);

create table if not exists generos (
    id serial primary key,
    nome varchar(50) not null
);

create table if not exists tipos_user (
    id serial primary key,
    nome varchar(50) not null
);

create table if not exists cores (
    id serial primary key,
    nome varchar(50) not null,
    codigo varchar(7) not null
);

create table if not exists enderecos (
    id serial primary key,
    logradouro varchar(200),
    numero varchar(20),
    bairro varchar(100),
    cidade varchar(100),
    estado varchar(2),
    cep varchar(10)
);

create table if not exists clientes (
    id serial primary key,
    clinica_id integer not null references clinicas(id),
    psicolog_id integer not null references psicologos(id),
    nome varchar(200) not null,
    status boolean not null default true
);

-- Inserir dados básicos
insert into categorias (nome) values ('Autônomo'), ('Sublocatário'), ('Clínica') on conflict do nothing;
insert into generos (nome) values ('Masculino'), ('Feminino'), ('Outro') on conflict do nothing;
insert into tipos_user (nome) values ('Admin'), ('Comum'), ('ComumSuper'), ('Autônomo') on conflict do nothing;
insert into tipos_pagamento (nome) values ('Dinheiro'), ('Cartão'), ('PIX'), ('Transferência') on conflict do nothing;
