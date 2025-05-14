package com.example.raw.dc.lab3.database

enum class Messages(private val col: String) {
	TABLE_NAME(
		"tbl_message_by_country"
	),
	COLUMN_ID(
		"id"
	),
	COLUMN_STORY_ID(
		"story_id"
	),
	COLUMN_CONTENT(
		"content"
	),
	COLUMN_COUNTRY(
		"country"
	);

	override fun toString(): String = col

	companion object {
		val SELECT_MESSAGES: String = """
			SELECT
				$COLUMN_COUNTRY,
				$COLUMN_STORY_ID,
				$COLUMN_ID,
				$COLUMN_CONTENT
			FROM $TABLE_NAME;
			""".trimIndent()
	}
}