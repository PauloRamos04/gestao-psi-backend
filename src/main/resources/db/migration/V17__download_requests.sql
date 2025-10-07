-- Tabela de requisições de download
CREATE TABLE IF NOT EXISTS download_requests (
    id BIGSERIAL PRIMARY KEY,
    category VARCHAR(50) NOT NULL,
    type VARCHAR(20) NOT NULL,
    inicio VARCHAR(20),
    fim VARCHAR(20),
    status VARCHAR(20) NOT NULL DEFAULT 'pending',
    file_path VARCHAR(500),
    file_name VARCHAR(255),
    file_size BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP,
    error_message TEXT,
    user_id BIGINT
);

CREATE INDEX idx_download_requests_status ON download_requests(status);
CREATE INDEX idx_download_requests_user_id ON download_requests(user_id);
CREATE INDEX idx_download_requests_created_at ON download_requests(created_at DESC);

