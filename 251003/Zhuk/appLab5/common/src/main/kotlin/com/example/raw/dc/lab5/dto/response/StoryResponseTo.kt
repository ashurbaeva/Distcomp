package com.example.raw.dc.lab5.dto.response

import com.example.raw.dc.lab5.util.TimeStampSerializer
import kotlinx.serialization.Serializable
import java.sql.Timestamp

@Serializable
data class StoryResponseTo(
	private val id: Long?,
	private val writerId: Long,
	private val title: String,
	private val content: String,
	@Serializable(TimeStampSerializer::class) private val created: Timestamp,
	@Serializable(TimeStampSerializer::class) private val modified: Timestamp
)