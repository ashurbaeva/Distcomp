package com.example.raw.dc.lab5.dto.request

import com.example.raw.dc.lab5.bean.Message
import kotlinx.serialization.Serializable

@Serializable
data class MessageRequestTo(
	private val storyId: Long, val content: String
) {
	fun toBean(id: Long?, country: String?): Message = Message(
		id, country, storyId, content
	)

	init {
		require(content.length in 4..2048)
	}
}