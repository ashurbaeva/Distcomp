package com.example.raw.dc.lab4.database

enum class Markers(private val col: String) {
	TABLE_NAME(
		"tbl_marker"
	),
	COLUMN_ID(
		"id"
	),
	COLUMN_NAME(
		"name"
	);

	override fun toString(): String = col

	companion object {
		val CREATE_TABLE_MARKERS: String = """
			CREATE TABLE IF NOT EXISTS $TABLE_NAME (
				$COLUMN_ID SERIAL PRIMARY KEY,
				$COLUMN_NAME VARCHAR(32)
			);
			""".trimIndent()

		val INSERT_MARKER: String = """
			INSERT INTO $TABLE_NAME (
				$COLUMN_NAME
			) VALUES (?);
			""".trimIndent()

		val SELECT_MARKER_BY_ID: String = """
			SELECT
				$COLUMN_NAME
			FROM $TABLE_NAME
			WHERE $COLUMN_ID = ?;
			""".trimIndent()

		val SELECT_MARKERS: String = """
			SELECT
				$COLUMN_ID,
				$COLUMN_NAME
			FROM $TABLE_NAME
			""".trimIndent()

		val UPDATE_MARKER: String = """
			UPDATE $TABLE_NAME
			SET $COLUMN_NAME = ?
			WHERE $COLUMN_ID = ?;
			""".trimIndent()

		val DELETE_MARKER: String = """
			DELETE FROM $TABLE_NAME
			WHERE $COLUMN_ID = ?;
			""".trimIndent()
	}
}