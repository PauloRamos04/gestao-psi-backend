create table if not exists pacientes (
    id serial primary key,
    clinica_id integer not null references clinicas(id),
    psicolog_id integer not null references psicologos(id),
    nome varchar(200) not null,
    status boolean not null default true
);

create table if not exists salas (
    id serial primary key,
    clinica_id integer not null references clinicas(id),
    nome varchar(100) not null
);

create table if not exists sessoes (
    id serial primary key,
    clinica_id integer not null references clinicas(id),
    psicolog_id integer not null references psicologos(id),
    paciente_id integer not null references pacientes(id),
    sala_id integer references salas(id),
    data date not null,
    hora time not null,
    status boolean not null default true
);

create index if not exists idx_sessoes_data on sessoes(data);
create index if not exists idx_pacientes_nome on pacientes(nome);


