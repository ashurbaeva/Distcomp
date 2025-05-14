package com.example.eger.dc.lab5.dto.response

import com.example.eger.dc.lab5.util.TimeStampSerializer
import kotlinx.serialization.Serializable
import java.sql.Timestamp

@Serializable
data class StoryResponseTo(
	private val id: Long?,
	private val authorId: Long,
	private val title: String,
	private val content: String,
	@Serializable(TimeStampSerializer::class) private val created: Timestamp,
	@Serializable(TimeStampSerializer::class) private val modified: Timestamp
)