package com.example.raw.dc.lab1.bean

import com.example.raw.dc.lab1.dto.response.StorieResponseTo
import com.example.raw.dc.lab1.util.TimeStampSerializer
import kotlinx.serialization.Serializable
import java.sql.Timestamp

@Serializable
data class Story(
	val id: Long,
	val writerId: Long,
	val title: String,
	val content: String,
	@Serializable(TimeStampSerializer::class) val created: Timestamp,
	@Serializable(TimeStampSerializer::class) val modified: Timestamp
) {
	fun toResponse(): StorieResponseTo =
        StorieResponseTo(
            id,
            writerId,
            title,
            content,
            created,
            modified
        )
}