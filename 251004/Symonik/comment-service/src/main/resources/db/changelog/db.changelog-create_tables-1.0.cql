--liquibase formatted sql

--changeset symonik:1
CREATE KEYSPACE IF NOT EXISTS issue_keyspace
            WITH replication = { 'class': 'SimpleStrategy', 'replication_factor': 1};

USE issue_keyspace;

CREATE TABLE tbl_comment
(
    country  text,
    id       bigint,
    issue_id bigint,
    content  text,
    PRIMARY KEY ((country), id, issue_id)
);