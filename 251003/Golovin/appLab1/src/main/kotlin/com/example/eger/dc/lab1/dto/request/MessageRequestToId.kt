package com.example.eger.dc.lab1.dto.request

import com.example.eger.dc.lab1.bean.Message
import kotlinx.serialization.Serializable

@Serializable
data class MessageRequestToId(
	private val id: Long, private val storyId: Long, private val content: String
) {
	fun toBean(): Message = Message(
		id, storyId, content
	)

	init {
		require(content.length in 4..2048)
	}
}