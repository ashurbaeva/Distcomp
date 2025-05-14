package com.example.raw.dc.lab2.bean

import com.example.raw.dc.lab2.dto.response.StoryResponseTo
import com.example.raw.dc.lab2.util.TimeStampSerializer
import kotlinx.serialization.Serializable
import java.sql.Timestamp

@Serializable
data class Story(
	val id: Long?,
	val writerId: Long,
	val title: String,
	val content: String,
	@Serializable(TimeStampSerializer::class) val created: Timestamp,
	@Serializable(TimeStampSerializer::class) val modified: Timestamp,
	val markers: List<String>? = null
) {
	fun toResponse(): StoryResponseTo = StoryResponseTo(id, writerId, title, content, created, modified, markers)
}