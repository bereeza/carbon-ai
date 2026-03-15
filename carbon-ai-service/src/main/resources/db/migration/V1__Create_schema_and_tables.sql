-- Create carbon schema
CREATE SCHEMA IF NOT EXISTS carbon;

-- Enable pgvector extension for vector operations
CREATE EXTENSION IF NOT EXISTS vector;

-- Create processed_content table
CREATE TABLE IF NOT EXISTS carbon.processed_content (
    id BIGSERIAL PRIMARY KEY,
    original_id UUID NOT NULL,
    summary VARCHAR(4000) NOT NULL,
    embedding vector(1536)
);

-- Create search_logs table
CREATE TABLE IF NOT EXISTS carbon.search_logs (
    id BIGSERIAL PRIMARY KEY,
    query VARCHAR(1000) NOT NULL,
    searched_at TIMESTAMP NOT NULL,
    user_id VARCHAR(255)
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_processed_content_original_id ON carbon.processed_content(original_id);
CREATE INDEX IF NOT EXISTS idx_search_logs_user_id ON carbon.search_logs(user_id);
CREATE INDEX IF NOT EXISTS idx_search_logs_searched_at ON carbon.search_logs(searched_at);
