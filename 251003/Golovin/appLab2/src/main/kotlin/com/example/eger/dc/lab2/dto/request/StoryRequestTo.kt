package com.example.eger.dc.lab2.dto.request

import com.example.eger.dc.lab2.bean.Story
import kotlinx.serialization.Serializable
import java.sql.Timestamp

@Serializable
data class StoryRequestTo(
	private val authorId: Long, private val title: String, private val content: String, private val labels: List<String>?= null
) {
	fun toBean(id: Long?, created: Timestamp, modified: Timestamp): Story = Story(
		id, authorId, title, content, created, modified,labels
	)

	init {
		require(title.length in 2..64)
		require(content.length in 4..2048)
	}
}