package com.example.eger.dc.lab4.dto.request

import com.example.eger.dc.lab4.bean.Message
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