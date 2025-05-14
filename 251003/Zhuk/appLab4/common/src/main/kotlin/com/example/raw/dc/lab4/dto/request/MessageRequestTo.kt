package com.example.raw.dc.lab4.dto.request

import com.example.raw.dc.lab4.bean.Message
import kotlinx.serialization.Serializable

@Serializable
data class MessageRequestTo(
	private val storyId: Long, private val content: String
) {
	fun toBean(id: Long?, country: String?): Message = Message(
		id, country, storyId, content
	)

	init {
		require(content.length in 4..2048)
	}
}