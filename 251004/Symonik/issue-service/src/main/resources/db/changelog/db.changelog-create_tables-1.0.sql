--liquibase formatted sql

--changeset symonik:1
CREATE TABLE tbl_editor
(
    id        SERIAL PRIMARY KEY,
    login     VARCHAR(64) UNIQUE NOT NULL,
    password  VARCHAR(128)       NOT NULL,
    firstname VARCHAR(64)        NOT NULL,
    lastname  VARCHAR(64)        NOT NULL
);

CREATE TABLE tbl_label
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(32) NOT NULL
);

CREATE TABLE tbl_issue
(
    id                 SERIAL PRIMARY KEY,
    editor_id         BIGINT        NOT NULL,
    title              VARCHAR(64)   NOT NULL,
    content            VARCHAR(2048) NOT NULL,
    created_time       DATE DEFAULT CURRENT_TIMESTAMP,
    last_modified_time DATE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_editor FOREIGN KEY (editor_id) REFERENCES tbl_editor (id) ON DELETE SET NULL
);

CREATE TABLE tbl_comment
(
    id       SERIAL PRIMARY KEY,
    issue_id BIGINT        NOT NULL,
    content  VARCHAR(2048) NOT NULL,
    CONSTRAINT fk_issue FOREIGN KEY (issue_id) REFERENCES tbl_issue (id) ON DELETE CASCADE
);