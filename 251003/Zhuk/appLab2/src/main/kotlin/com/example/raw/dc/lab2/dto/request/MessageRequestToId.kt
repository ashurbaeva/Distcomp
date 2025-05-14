package com.example.raw.dc.lab2.dto.request

import com.example.raw.dc.lab2.bean.Message
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