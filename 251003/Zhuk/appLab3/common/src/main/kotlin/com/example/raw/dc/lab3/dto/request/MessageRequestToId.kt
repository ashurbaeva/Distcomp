package com.example.raw.dc.lab3.dto.request

import com.example.raw.dc.lab3.bean.Message
import kotlinx.serialization.Serializable

@Serializable
data class MessageRequestToId(
	private val id: Long, private val storyId: Long, private val content: String
) {
	fun toBean(country: String?): Message = Message(
		id, country, storyId, content
	)

	init {
		require(content.length in 4..2048)
	}
}