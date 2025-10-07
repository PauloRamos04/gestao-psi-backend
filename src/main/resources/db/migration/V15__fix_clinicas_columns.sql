-- Corrigir colunas da tabela clinicas

DO $$
BEGIN
    -- nome
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_name='clinicas' AND column_name='nome') THEN
        ALTER TABLE clinicas ADD COLUMN nome VARCHAR(200) NOT NULL DEFAULT 'Clínica';
    END IF;

    -- bloco
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_name='clinicas' AND column_name='bloco') THEN
        ALTER TABLE clinicas ADD COLUMN bloco VARCHAR(50);
    END IF;

    -- capacidade
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_name='clinicas' AND column_name='capacidade') THEN
        ALTER TABLE clinicas ADD COLUMN capacidade INTEGER;
    END IF;

    -- cidade
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_name='clinicas' AND column_name='cidade') THEN
        ALTER TABLE clinicas ADD COLUMN cidade VARCHAR(100);
    END IF;

    -- descricao
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_name='clinicas' AND column_name='descricao') THEN
        ALTER TABLE clinicas ADD COLUMN descricao TEXT;
    END IF;

    -- exclusive
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_name='clinicas' AND column_name='exclusive') THEN
        ALTER TABLE clinicas ADD COLUMN exclusive BOOLEAN DEFAULT FALSE;
    END IF;

    -- numero
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_name='clinicas' AND column_name='numero') THEN
        ALTER TABLE clinicas ADD COLUMN numero VARCHAR(20);
    END IF;

    -- observacoes
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_name='clinicas' AND column_name='observacoes') THEN
        ALTER TABLE clinicas ADD COLUMN observacoes TEXT;
    END IF;

    -- permite_compartilhamento
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_name='clinicas' AND column_name='permite_compartilhamento') THEN
        ALTER TABLE clinicas ADD COLUMN permite_compartilhamento BOOLEAN DEFAULT TRUE;
    END IF;

    -- cor
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_name='clinicas' AND column_name='cor') THEN
        ALTER TABLE clinicas ADD COLUMN cor VARCHAR(7);
    END IF;

    -- psicolog_responsavel_id
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_name='clinicas' AND column_name='psicolog_responsavel_id') THEN
        ALTER TABLE clinicas ADD COLUMN psicolog_responsavel_id INTEGER REFERENCES psicologos(id);
    END IF;
END $$;

