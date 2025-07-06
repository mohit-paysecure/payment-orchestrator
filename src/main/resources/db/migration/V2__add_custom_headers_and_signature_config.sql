DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'provider_config' AND column_name = 'custom_headers'
    ) THEN
ALTER TABLE provider_config
    ADD COLUMN custom_headers jsonb;
END IF;
END;
$$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'provider_config' AND column_name = 'signature_config'
    ) THEN
ALTER TABLE provider_config
    ADD COLUMN signature_config jsonb;
END IF;
END;
$$;
