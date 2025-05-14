--liquibase formatted sql

--changeset symonik:1
CREATE TABLE m2m_issue_label
(
    label_id  BIGINT NOT NULL,
    issue_id BIGINT NOT NULL,
    CONSTRAINT fk_label FOREIGN KEY (label_id) REFERENCES tbl_label (id) ON DELETE CASCADE,
    CONSTRAINT fk_issue FOREIGN KEY (issue_id) REFERENCES tbl_issue (id) ON DELETE CASCADE
);