package com.example.raw.dc.lab1.dto.response

import com.example.raw.dc.lab1.util.TimeStampSerializer
import kotlinx.serialization.Serializable
import java.sql.Timestamp

@Serializable
data class StorieResponseTo(
	private val id: Long,
	private val writerId: Long,
	private val title: String,
	private val content: String,
	@Serializable(TimeStampSerializer::class) private val created: Timestamp,
	@Serializable(TimeStampSerializer::class) private val modified: Timestamp
)