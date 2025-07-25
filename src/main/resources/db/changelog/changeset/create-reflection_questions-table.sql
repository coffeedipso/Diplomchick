--changeset reflect-diary:2
CREATE TABLE reflection_questions (
    id UUID NOT NULL DEFAULT gen_random_uuid(),
    text VARCHAR(1000) NOT NULL,
    CONSTRAINT reflection_questions_pk PRIMARY KEY (id)
);

--rollback DROP TABLE reflection_questions;