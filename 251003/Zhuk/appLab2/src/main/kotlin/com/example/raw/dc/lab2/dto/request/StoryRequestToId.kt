package com.example.raw.dc.lab2.dto.request

import com.example.raw.dc.lab2.bean.Story
import kotlinx.serialization.Serializable
import java.sql.Timestamp

@Serializable
data class StoryRequestToId(
	private val id: Long, private val writerId: Long, private val title: String, private val content: String, private val markers: List<String>?= null
) {
	fun toBean(created: Timestamp, modified: Timestamp): Story = Story(
		id, writerId, title, content, created, modified, markers
	)

	init {
		require(title.length in 2..64)
		require(content.length in 4..2048)
	}
}