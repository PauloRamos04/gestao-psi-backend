-- Tabela de Prontuários Eletrônicos
create table if not exists prontuarios (
    id serial primary key,
    paciente_id integer not null references pacientes(id),
    sessao_id integer references sessoes(id),
    psicolog_id integer not null references psicologos(id),
    data_registro timestamp not null default current_timestamp,
    tipo varchar(50) not null,
    titulo varchar(200),
    conteudo text not null,
    queixa_principal varchar(500),
    objetivo_terapeutico varchar(500),
    historico text,
    evolucao text,
    plano_terapeutico text,
    privado boolean not null default true,
    status boolean not null default true
);

-- Índices para performance
create index if not exists idx_prontuarios_paciente on prontuarios(paciente_id);
create index if not exists idx_prontuarios_psicologo on prontuarios(psicolog_id);
create index if not exists idx_prontuarios_data on prontuarios(data_registro);
create index if not exists idx_prontuarios_tipo on prontuarios(tipo);

