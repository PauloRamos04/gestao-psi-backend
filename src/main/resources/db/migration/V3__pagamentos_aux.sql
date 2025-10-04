create table if not exists tipos_pagamento (
    id serial primary key,
    nome varchar(100) not null
);

create table if not exists pagamentos (
    id serial primary key,
    clinica_id integer not null references clinicas(id),
    psicolog_id integer not null references psicologos(id),
    paciente_id integer not null references pacientes(id),
    valor numeric(12,2) not null,
    data date not null,
    tipo_pagamento_id integer references tipos_pagamento(id)
);

create index if not exists idx_pagamentos_data on pagamentos(data);


