create table if not exists mensagens (
    id serial primary key,
    titulo varchar(200) not null,
    conteudo text,
    data_criacao timestamp not null default current_timestamp,
    status boolean not null default true
);
