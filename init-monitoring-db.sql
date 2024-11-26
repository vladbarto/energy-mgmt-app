CREATE USER vladbarto WITH PASSWORD '1234';
CREATE DATABASE "monitoring-db" WITH OWNER vladbarto;
GRANT ALL PRIVILEGES ON DATABASE "monitoring-db" TO vladbarto;