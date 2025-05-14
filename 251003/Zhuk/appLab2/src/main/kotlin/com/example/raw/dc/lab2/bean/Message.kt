package com.example.raw.dc.lab2.bean

import com.example.raw.dc.lab2.dto.response.MessageResponseTo
import kotlinx.serialization.Serializable

@Serializable
data class Message(
	val id: Long?, val storyId: Long, val content: String
) {
	fun toResponse(): MessageResponseTo = MessageResponseTo(id, storyId, content)
}