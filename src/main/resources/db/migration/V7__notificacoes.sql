-- Tabela de Notificações
create table if not exists notificacoes (
    id serial primary key,
    -- Referencia a tabela correta de usuários ("users")
    usuario_id integer not null references users(id),
    titulo varchar(200) not null,
    mensagem text not null,
    tipo varchar(50) default 'INFO',
    data_criacao timestamp not null default current_timestamp,
    data_leitura timestamp,
    lida boolean not null default false,
    link varchar(500),
    enviada boolean not null default false,
    data_envio timestamp
);

-- Índices para performance
create index if not exists idx_notificacoes_usuario on notificacoes(usuario_id);
create index if not exists idx_notificacoes_lida on notificacoes(lida);
create index if not exists idx_notificacoes_tipo on notificacoes(tipo);
create index if not exists idx_notificacoes_data on notificacoes(data_criacao);

