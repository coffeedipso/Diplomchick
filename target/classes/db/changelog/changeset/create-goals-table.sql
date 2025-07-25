--changeset reflect-diary:4
CREATE TABLE goals (
    id UUID NOT NULL DEFAULT gen_random_uuid(),
    description VARCHAR(10000) NOT NULL,
    is_completed BOOLEAN DEFAULT false,
    deadline DATE NOT NULL,
    user_id UUID,
    CONSTRAINT goals_pk PRIMARY KEY (id),
    CONSTRAINT goals_user_fk FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

--rollback DROP TABLE goals;