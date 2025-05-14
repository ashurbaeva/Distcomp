package com.example.eger.dc.lab2.bean

import com.example.eger.dc.lab2.dto.response.StoryResponseTo
import com.example.eger.dc.lab2.util.TimeStampSerializer
import kotlinx.serialization.Serializable
import java.sql.Timestamp

@Serializable
data class Story(
	val id: Long?,
	val authorId: Long,
	val title: String,
	val content: String,
	@Serializable(TimeStampSerializer::class) val created: Timestamp,
	@Serializable(TimeStampSerializer::class) val modified: Timestamp,
	val labels: List<String>? = null
) {
	fun toResponse(): StoryResponseTo =
		StoryResponseTo(id, authorId, title, content, created, modified, labels)
}