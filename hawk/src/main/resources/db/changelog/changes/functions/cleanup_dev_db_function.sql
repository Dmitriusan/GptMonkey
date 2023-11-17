CREATE OR REPLACE FUNCTION cleanup_dev_db() RETURNS VOID AS $$
DECLARE
    table_name text;
    sequence_name text;
    type_name text;
BEGIN
    -- Loop through and drop all tables in the merchandise_db schema
    FOR table_name IN (SELECT tablename FROM pg_tables WHERE schemaname = 'merchandise_db') LOOP
            EXECUTE 'DROP TABLE IF EXISTS merchandise_db.' || table_name || ' CASCADE';
        END LOOP;

    -- Loop through and drop all sequences in the merchandise_db schema
    FOR sequence_name IN (SELECT sequencename FROM pg_sequences WHERE schemaname = 'merchandise_db') LOOP
            EXECUTE 'DROP SEQUENCE IF EXISTS merchandise_db.' || sequence_name;
        END LOOP;

    -- Loop through and drop all user-defined types in the merchandise_db schema
    FOR type_name IN (SELECT typname FROM pg_type WHERE typnamespace = (SELECT oid FROM pg_namespace WHERE nspname = 'merchandise_db')) LOOP
            EXECUTE 'DROP TYPE IF EXISTS merchandise_db.' || type_name;
        END LOOP;
END;
$$ LANGUAGE plpgsql;