package com.example.eger.dc.lab1.dto.request

import com.example.eger.dc.lab1.bean.Story
import kotlinx.serialization.Serializable
import java.sql.Timestamp

@Serializable
data class StoryRequestToId(
	private val id: Long, private val writerId: Long, private val title: String, private val content: String
) {
	fun toBean(created: Timestamp, modified: Timestamp): Story = Story(
		id, writerId, title, content, created, modified
	)

	init {
		require(title.length in 2..64)
		require(content.length in 4..2048)
	}
}