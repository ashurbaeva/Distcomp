CREATE OR REPLACE FUNCTION cleanup_unused_labels_trigger() RETURNS TRIGGER
    LANGUAGE plpgsql AS $$
BEGIN
    DELETE FROM tbl_label
    WHERE id NOT IN (SELECT DISTINCT label_id FROM m2m_issue_label);
    RETURN NULL;
END; $$;

CREATE OR REPLACE TRIGGER cleanup_unused_labels
    AFTER DELETE
    ON m2m_issue_label
    FOR EACH ROW EXECUTE FUNCTION cleanup_unused_labels_trigger();