-- V22: Tornar tipo_id nullable na tabela users
-- O campo tipo_id agora é opcional, pois o sistema migrou para roles

ALTER TABLE users MODIFY COLUMN tipo_id BIGINT NULL;


