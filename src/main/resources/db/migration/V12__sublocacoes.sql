create table if not exists sublocacoes (
    id serial primary key,
    clinica_id integer not null references clinicas(id),
    psicolog_id integer references psicologos(id),
    room_name varchar(200) not null,
    tenant_name varchar(200) not null,
    tenant_type varchar(30) not null,
    start_date date not null,
    end_date date not null,
    monthly_rate numeric(12,2) not null,
    status varchar(20) not null default 'active',
    total_sessions integer not null default 0,
    total_revenue numeric(12,2) not null default 0,
    last_payment date,
    next_payment date
);

create index if not exists idx_sublocacoes_clinica on sublocacoes(clinica_id);



