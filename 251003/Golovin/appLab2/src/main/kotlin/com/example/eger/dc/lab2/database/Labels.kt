package com.example.eger.dc.lab2.database

enum class Labels(private val col: String) {
	TABLE_NAME(
		"tbl_label"
	),
	COLUMN_ID(
		"id"
	),
	COLUMN_NAME(
		"name"
	);

	override fun toString(): String = col

	companion object {
		val CREATE_TABLE_LABELS: String = """
			CREATE TABLE IF NOT EXISTS $TABLE_NAME (
				$COLUMN_ID SERIAL PRIMARY KEY,
				$COLUMN_NAME VARCHAR(32)
			);
			""".trimIndent()

		val INSERT_LABEL: String = """
			INSERT INTO $TABLE_NAME (
				$COLUMN_NAME
           )VALUES (?)
            ON CONFLICT DO NOTHING;
        """.trimIndent()


		val SELECT_LABEL_ID_BY_NAME = """
            SELECT $COLUMN_ID FROM $TABLE_NAME WHERE $COLUMN_NAME = ?;
        """.trimIndent()

		val SELECT_LABEL_BY_ID: String = """
			SELECT
				$COLUMN_NAME
			FROM $TABLE_NAME
			WHERE $COLUMN_ID = ?;
			""".trimIndent()

		val SELECT_LABELS: String = """
			SELECT
				$COLUMN_ID,
				$COLUMN_NAME
			FROM $TABLE_NAME
			""".trimIndent()

		val UPDATE_LABEL: String = """
			UPDATE $TABLE_NAME
			SET $COLUMN_NAME = ?
			WHERE $COLUMN_ID = ?;
			""".trimIndent()

		val DELETE_LABEL: String = """
			DELETE FROM $TABLE_NAME
			WHERE $COLUMN_ID = ?;
			""".trimIndent()
	}
}