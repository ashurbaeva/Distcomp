package com.example.eger.dc.lab5.dto.request

import com.example.eger.dc.lab5.bean.Story
import kotlinx.serialization.Serializable
import java.sql.Timestamp

@Serializable
data class StoryRequestTo(
	private val authorId: Long, private val title: String, private val content: String
) {
	fun toBean(id: Long?, created: Timestamp, modified: Timestamp): Story = Story(
		id, authorId, title, content, created, modified
	)

	init {
		require(title.length in 2..64)
		require(content.length in 4..2048)
	}
}