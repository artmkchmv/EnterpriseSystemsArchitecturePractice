SET client_encoding = 'UTF8';

DROP TABLE IF EXISTS change_log;

CREATE TABLE change_log (
    id           SERIAL PRIMARY KEY,
    change_type  VARCHAR(10)  NOT NULL,
    entity_class VARCHAR(100) NOT NULL,
    entity_id    INTEGER      NOT NULL,
    details      TEXT,
    changed_at   TIMESTAMP    NOT NULL DEFAULT now()
);
