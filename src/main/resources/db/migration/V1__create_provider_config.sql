CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE provider_config (
                                 id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                 name TEXT NOT NULL,
                                 base_url TEXT NOT NULL,
                                 auth_type TEXT NOT NULL,
                                 auth_key TEXT,
                                 request_template JSONB,
                                 response_mapping JSONB
);
