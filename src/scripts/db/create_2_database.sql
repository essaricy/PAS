-- dropdb.exe --host=localhost --port=5432 --username=postgres SV_PMS
-- DROP DATABASE SV_PMS;

CREATE DATABASE SV_PMS
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;
