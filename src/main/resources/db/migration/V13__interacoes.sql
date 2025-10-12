create table if not exists interacoes_sugestoes (
    id serial primary key,
    title varchar(150) not null,
    content text not null,
    type varchar(20),
    status varchar(20),
    priority varchar(10),
    author varchar(100),
    date date,
    response varchar(1000),
    response_date date
);

create table if not exists interacoes_indicacoes (
    id serial primary key,
    name varchar(120) not null,
    profession varchar(120) not null,
    contact varchar(120) not null,
    location varchar(120),
    description varchar(1000),
    status varchar(20),
    date date
);



