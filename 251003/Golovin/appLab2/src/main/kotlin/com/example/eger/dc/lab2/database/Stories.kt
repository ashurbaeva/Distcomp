package com.example.eger.dc.lab2.database


enum class Stories(private val col: String) {

	TABLE_NAME(
		"tbl_story"
	),
	COLUMN_ID(
		"id"
	),
	COLUMN_AUTHOR_ID(
		"author_id"
	),
	COLUMN_TITLE(
		"title"
	),
	COLUMN_CONTENT(
		"content"
	),
	COLUMN_CREATED(
		"created"
	),
	COLUMN_MODIFIED(
		"modified"
	);

	override fun toString(): String = col

	companion object {
		val CREATE_TABLE_STORIES: String = """
			CREATE TABLE IF NOT EXISTS $TABLE_NAME (
				$COLUMN_ID SERIAL PRIMARY KEY,
				$COLUMN_AUTHOR_ID BIGINT,
				$COLUMN_TITLE VARCHAR(64) UNIQUE,
				$COLUMN_CONTENT VARCHAR(2048),
				$COLUMN_CREATED TIMESTAMP,
				$COLUMN_MODIFIED TIMESTAMP,
				FOREIGN KEY($COLUMN_AUTHOR_ID) REFERENCES ${Authors.TABLE_NAME} (${Authors.COLUMN_ID}) ON UPDATE NO ACTION
			);
			""".trimIndent()

		const val TABLE_STORY_LABEL = "tbl_story_label"
		const val COLUMN_LABEL_ID = "label_id"
		val CREATE_TABLE_STORY_LABELS = """
    CREATE TABLE IF NOT EXISTS $TABLE_STORY_LABEL (
        $COLUMN_ID BIGINT,
        $COLUMN_LABEL_ID BIGINT,
        PRIMARY KEY ($COLUMN_ID, $COLUMN_LABEL_ID),
        FOREIGN KEY ($COLUMN_ID) REFERENCES $TABLE_NAME($COLUMN_ID) ON DELETE CASCADE,
        FOREIGN KEY ($COLUMN_LABEL_ID) REFERENCES ${Labels.TABLE_NAME}(${Labels.COLUMN_ID}) ON DELETE CASCADE
    );
""".trimIndent()

		val INSERT_STORY_LABEL = """
    INSERT INTO $TABLE_STORY_LABEL ($COLUMN_ID, $COLUMN_LABEL_ID)
    VALUES (?, ?)
    ON CONFLICT DO NOTHING;
""".trimIndent()

		val INSERT_STORY: String = """
			INSERT INTO $TABLE_NAME (
				$COLUMN_AUTHOR_ID,
				$COLUMN_TITLE,
				$COLUMN_CONTENT,
				$COLUMN_CREATED,
				$COLUMN_MODIFIED
			) VALUES (?, ?, ?, ?, ?);
			""".trimIndent()

		val SELECT_STORY_BY_ID: String = """
			SELECT
				$COLUMN_AUTHOR_ID,
				$COLUMN_TITLE,
				$COLUMN_CONTENT,
				$COLUMN_CREATED,
				$COLUMN_MODIFIED
			FROM $TABLE_NAME
			WHERE $COLUMN_ID = ?;
			""".trimIndent()

		val SELECT_STORIES: String = """
			SELECT
				$COLUMN_ID,
				$COLUMN_AUTHOR_ID,
				$COLUMN_TITLE,
				$COLUMN_CONTENT,
				$COLUMN_CREATED,
				$COLUMN_MODIFIED
			FROM $TABLE_NAME;
			""".trimIndent()

		val UPDATE_STORY: String = """
			UPDATE $TABLE_NAME
			SET
				$COLUMN_AUTHOR_ID = ?,
				$COLUMN_TITLE = ?,
				$COLUMN_CONTENT = ?,
				$COLUMN_CREATED = ?,
				$COLUMN_MODIFIED = ?
			WHERE $COLUMN_ID = ?;
			""".trimIndent()

		val DELETE_ALL_UNUSED_LABELS: String = """
    DELETE FROM tbl_label
    WHERE id NOT IN (
        SELECT label_id FROM tbl_story_label
    );
""".trimIndent()

		val DELETE_STORY_LABELS_BY_STORY_ID = """
    DELETE FROM tbl_story_label WHERE story_id = ?;
""".trimIndent()

		val DELETE_STORY = """
    DELETE FROM tbl_story WHERE id = ?;
""".trimIndent()
	}
}