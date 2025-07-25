--changeset reflect-diary:5
CREATE TABLE entries (
    id UUID NOT NULL DEFAULT gen_random_uuid(),
    date DATE NOT NULL,
    main_text VARCHAR(10000) NOT NULL,
    highlights VARCHAR(1000),
    difficulties VARCHAR(1000),
    tomorrow_plan VARCHAR(1000),
    score INT,
    mood VARCHAR(20) NOT NULL,
    created_at TIMESTAMP,
    user_id UUID,
    reflection_question_id UUID,
    question_answer VARCHAR(10000),
    CONSTRAINT entries_pk PRIMARY KEY (id),
    CONSTRAINT entries_user_fk FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT entries_reflection_question_fk FOREIGN KEY (reflection_question_id) REFERENCES reflection_questions(id)
);

--rollback DROP TABLE entries;