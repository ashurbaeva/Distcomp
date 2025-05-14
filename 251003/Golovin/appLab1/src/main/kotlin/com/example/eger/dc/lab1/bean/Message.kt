package com.example.eger.dc.lab1.bean

import com.example.eger.dc.lab1.dto.response.MessageResponseTo
import kotlinx.serialization.Serializable

@Serializable
data class Message(
	val id: Long, val storyId: Long, val content: String
) {
	fun toResponse(): MessageResponseTo = MessageResponseTo(id, storyId, content)
}