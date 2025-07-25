--liquibase formatted sql
--changeset reflect-diary:1
CREATE TABLE users (
    id UUID NOT NULL DEFAULT gen_random_uuid(),
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    role VARCHAR(20),
    registration_date TIMESTAMP,
    dark_mode_preference BOOLEAN DEFAULT false,
    CONSTRAINT users_pk PRIMARY KEY (id)
);
--rollback DROP TABLE users;
