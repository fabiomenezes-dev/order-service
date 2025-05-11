CREATE SCHEMA IF NOT EXISTS myschema;

CREATE TABLE IF NOT EXISTS myschema.orders (
    id SERIAL PRIMARY KEY,
    customer_name VARCHAR(100),
    total NUMERIC
);
